package ru.netology.nmedia.data

import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.mapper.toEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import ru.netology.nmedia.common.NullBodyException
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.mapper.toRequestBody
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.UpdatePostDto

class PostsRepositoryImpl(
    private val api: ApiService,
    private val postDao: PostDao
) : PostRepository {

    override fun send(newPostDto: NewPostDto, callback: PostRepository.Callback<Post>) {

        api.send(newPostDto.toRequestBody()).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onFailure(RuntimeException(response.message()))
                    return
                }

                response.body()?.let {
                    postDao.insert(it.toEntity(true))
                    callback.onSuccess(it)
                } ?: throw NullBodyException()

            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onFailure(RuntimeException(t.message.toString()))
            }
        })
    }

    override fun update(updatePostDto: UpdatePostDto): Post {

        val entity = postDao.update(updatePostDto.id, updatePostDto.content)

        api.send(updatePostDto.toRequestBody()).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) return
                postDao.syncData(updatePostDto.id)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                log(t.message)
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

        val isLiked = postDao.likeById(id)

        if (isLiked) {
            api.like(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    postDao.syncData(id)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    log(t.message)
                }

            })
        } else {
            api.unlike(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    postDao.syncData(id)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    log(t.message)
                }
            })
        }

        return postDao.getById(id).toModel()
    }

    override fun remove(id: Long) {

        postDao.setRemoved(id)

        api.remove(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                postDao.remove(id)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                log(t.message)
            }

        })

    }

    override fun syncData(callback: PostRepository.Callback<Unit>) {

        api.getAll().enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onFailure(RuntimeException(t.message.toString()))
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                if (!response.isSuccessful) {
                    callback.onFailure(NullBodyException())
                    return
                }

                val body = response.body() ?: throw NullBodyException()

                val remoteData = body.associateBy { it.id } // посты с сервера
                val localDataIds = postDao.getLocalPostIds() // айдишники локальных постов
                val removedIds = postDao.getRemoved() // айдишники удаленных постов

                localDataIds.forEach { if (!remoteData.containsKey(it)) postDao.remove(it) }

                remoteData.filter { !localDataIds.contains(it.key) }
                    .forEach {
                        when {
                            it.value.author != AUTHOR -> {
                                log(it.value.published)
                                postDao.insert(it.value.toEntity(true))
                            }
                            it.value.author == AUTHOR && removedIds.contains(it.key) ->
                                api.remove(it.key).enqueue(object : Callback<Unit> {
                                    override fun onResponse(
                                        call: Call<Unit>,
                                        response: Response<Unit>
                                    ) {
                                        postDao.remove(it.key)
                                    }

                                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                                        log(t.message)
                                    }

                                })
                            it.value.author == AUTHOR && !removedIds.contains(it.key) ->
                                postDao.insert(it.value.toEntity(true))
                        }
                    }

                val unsyncedPosts = postDao.getNotSynced()

                unsyncedPosts.forEach {
                    if (!it.likedByMe) {
                        api.like(it.id).enqueue(object : Callback<Post> {
                            override fun onFailure(call: Call<Post>, t: Throwable) {
                                log(t.message) // можно повторять запросы
                            }

                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                postDao.syncData(it.id)
                            }
                        })
                    } else {
                        api.unlike(it.id).enqueue(object : Callback<Post> {
                            override fun onFailure(call: Call<Post>, t: Throwable) {
                                log(t.message) // можно повторять запросы
                            }

                            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                postDao.syncData(it.id)
                            }
                        })
                    }
                    remoteData[it.id]?.content?.let { content ->
                        if (it.content != content) {
                            if (it.author == AUTHOR) {
                                api.send(
                                    PostRequestBody(id = it.id, content = it.content)
                                ).enqueue(object : Callback<Post> {
                                    override fun onFailure(call: Call<Post>, t: Throwable) {
                                        log(t.message)
                                    }

                                    override fun onResponse(
                                        call: Call<Post>,
                                        response: Response<Post>
                                    ) {
                                        postDao.syncData(it.id)
                                    }
                                })
                            } else {
                                postDao.update(it.id, content)
                                postDao.syncData(it.id)
                            }
                        }
                    }
                }
                callback.onSuccess(Unit)
            }
        })
    }
}