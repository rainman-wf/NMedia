package ru.netology.nmedia.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.domain.models.*

interface PostRepository {

    val posts: Flow<List<PostModel>>

    suspend fun getByKey(key: Long): PostModel
    suspend fun save(newPostDto: NewPostDto) : Long
    suspend fun sendPost(key: Long)
    suspend fun like(key: Long)
    suspend fun remove(key: Long)
    suspend fun update(updatePostDto: UpdatePostDto) : Long
    suspend fun syncData()
    suspend fun setRead(key: Long)
    fun getNewerCount()

}
