package ru.netology.nmedia.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.mapper.toEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.data.remote.RemoteDataSource
import ru.netology.nmedia.data.remote.dto.PostResponse
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

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

    override fun update(id: Long, content: String) : Post {

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

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {

        val localData = postDao.getAll()

        network.getAll(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onSuccess(localData.map { it.toModel() })
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        val postList = gson.fromJson<List<PostResponse>?>(body, typeToken.type)
                            .map { it.toModel() }
                        callback.onSuccess(syncData(localData, postList))
                    } catch (e: Exception) {
                        callback.onSuccess(localData.map { it.toModel() })
                    }
                }
            }
        )
    }

    override fun like(id: Long): Post {

        postDao.like(id)

        val entity = postDao.getById(id)

        if (!entity.isLiked) {
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

    private fun syncData(localData: List<PostEntity>, remoteData: List<Post>): List<Post> {

        val remoteMap = remoteData.associateBy { it.id }
        val localMap = localData.associateBy { it.id }

        val localFiltered = localMap.filter {
            if (!remoteMap.containsKey(it.key)) postDao.remove(it.key)
            remoteMap.containsKey(it.key)
        }.toMutableMap()

        remoteData.forEach {
            if (!localFiltered.containsKey(it.id)) {
                if (it.author == AUTHOR) {
                    network.remove(it.id, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            log("failure")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.code == 200) localFiltered.remove(it.id)
                        }

                    })

                } else {
                    localFiltered[postDao.insert(it.toEntity(true))] = it.toEntity(true)
                }
            }
        }

        return localFiltered.values.onEach {
            if (!it.syncStatus) {
                if (!it.isLiked) {
                    network.like(it.id, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            log("failure")
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
                            postDao.syncData(it.id)
                        }
                    })
                }
                remoteMap[it.id]?.content?.let { content ->
                    if (it.content != content) {
                        if (it.author === AUTHOR) {
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
                        }
                    }
                }
            }
        }.map { it.toModel() }
    }

}