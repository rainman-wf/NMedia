package ru.netology.nmedia.domain.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.models.*

interface PostRepository {

    val posts: LiveData<List<PostModel>>

    suspend fun getByKey(key: Long) : PostModel
    suspend fun save(newPostDto: NewPostDto)
    suspend fun sendPost(key: Long)
    suspend fun like(key: Long)
    suspend fun remove(key: Long)
    suspend fun update(updatePostDto: UpdatePostDto)
    suspend fun syncData()

}
