package ru.netology.nmedia.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.mapper.toEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.data.remote.RemoteDataSource
import ru.netology.nmedia.data.remote.dto.PostResponse
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import java.io.IOException
import java.lang.Exception

class PostsRepositoryImpl(
    private val network: RemoteDataSource,
    private val postDao: PostDao
) : PostRepository {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<PostResponse>>() {}

    override fun send(content: String, callback: PostRepository.Callback<Post>) {

        network.send(content, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: throw RuntimeException("body is null")
                try {
                    val post = gson.fromJson(body, PostResponse::class.java).toEntity()
                    callback.onSuccess(postDao.save(post).toModel())
                } catch (e: Exception) {
                    callback.onFailure(e)
                }
            }
        })
    }

    override fun update(id: Long, content: String): Post {

        val entity = postDao.update(id, content)

        network.send(content, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("failure")
            }

            override fun onResponse(call: Call, response: Response) {
                postDao.syncData(id)
            }
        })

        return entity.toModel()
    }

    override fun getById(id: Long): Post {
        return postDao.getById(id).toModel()
    }

    override fun getAll(): List<Post> {
        return postDao.getAll().map { it.toModel() }
    }

    override fun like(id: Long): Post {

        postDao.like(id)

        val entity = postDao.getById(id)

        if (entity.isLiked) {
            network.like(id, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log("failure")
                }

                override fun onResponse(call: Call, response: Response) {
                    log(response.body?.string())
                    postDao.syncData(id)
                }
            })
        } else {
            network.unlike(id, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log("failure")
                }

                override fun onResponse(call: Call, response: Response) {
                    log(response.body?.string())
                    postDao.syncData(id)
                }
            })
        }

        return entity.toModel()
    }

    override fun remove(id: Long): Int {

        val count = postDao.remove(id)

        network.remove(id, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log("failure")
            }

            override fun onResponse(call: Call, response: Response) {
                log("success")
            }
        })

        return count
    }

    override fun syncData(callback: PostRepository.Callback<Unit>) {

        network.getAll(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: throw RuntimeException("body is null")
                val remoteData = (gson.fromJson(body, typeToken.type)
                        as List<PostResponse>).associateBy { it.id }

                val localDataIds = postDao.getLocalPostIds()

                localDataIds.forEach { if (!remoteData.containsKey(it)) postDao.remove(it) }

                remoteData.forEach {

                    /* здесь я думал по какому принципу удалять посты, если автором являюсь я.
                    Пришел к такому выводу, что нужно создавать таблицу удаленных постов с ключем
                    по времени созданию (по сутия не мог никак создать в один момент времени два
                    поста, на сервере штамп времени будет разным в любом случае.

                    Мысль такая, что если автором поста являюсь я, и на моем устройстве он удален,
                    то удалить на сервере. Ведь в локальную базу данным по моему алгоритму пост мог
                    попасть только при положительном результате запроса на сервер.

                    Однако если пост был отправлен с другого устройства, то на этом устройстве
                    это пост никогда не появлялся, и при первом же соединении пост будет удален
                    на сервере. Поэтому нужно отфильтровать по имени автора, потом уже проверить,
                    не был ли пост удален с этого устройства, если нет, то добавить пост локально,
                    если же пост находится в списке удаленных, то удалить на сервере.

                    С постами чужих авторов все просто. Если поста нет удаленно, то его удалил автор,
                    значит нужно удалить локально.
                    */

                    if (!localDataIds.contains(it.key)) postDao.insert(it.value.toEntity())
                }

                val mergedData = postDao.getAll()

                mergedData
                    .filter { !it.syncStatus }
                    .forEach {
                    if (!it.syncStatus) {
                        if (!it.isLiked) {
                            network.like(it.id, object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    log("failure") // можно повторять запросы
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    postDao.syncData(it.id)
                                }
                            })
                        } else {
                            network.unlike(it.id, object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    log("failure")
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    postDao.syncData(it.id) // можно повторять запросы
                                }
                            })
                        }
                        remoteData[it.id]?.content?.let { content ->
                            if (it.content != content) {
                                if (it.author == AUTHOR) {
                                    network.updateContent(
                                        id = it.id,
                                        content = it.content,
                                        object : Callback {
                                            override fun onFailure(call: Call, e: IOException) {
                                                log("failure")
                                            }

                                            override fun onResponse(call: Call, response: Response) {
                                                postDao.syncData(it.id)
                                            }
                                        }
                                    )
                                } else {
                                    postDao.update(it.id, content)
                                    postDao.syncData(it.id)
                                }
                            }
                        }
                    }
                }
                callback.onSuccess(Unit)
            }
        })
    }
}