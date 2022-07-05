package ru.netology.nmedia.data.network

import ru.netology.nmedia.data.network.dto.PostResponse

interface NetworkService {
    fun getAll(): List<PostResponse>
    fun getByID(id: Long): PostResponse
    fun save(postResponse: PostResponse): PostResponse
    fun likeById(id: Long): PostResponse
    fun unlikeById(id: Long): PostResponse
    fun removeById(id: Long)
}