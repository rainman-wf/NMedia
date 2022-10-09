package ru.netology.nmedia.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.domain.models.*

interface PostRepository {

    val posts: Flow<PagingData<FeedItem>>

    suspend fun sendPost(newPostDto: NewPostDto)
    suspend fun update(updatePostDto: UpdatePostDto)
    suspend fun like(id: Long)
    suspend fun remove(id: Long)

}
