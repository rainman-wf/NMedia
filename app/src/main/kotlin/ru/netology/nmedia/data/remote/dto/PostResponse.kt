package ru.netology.nmedia.data.remote.dto

class PostResponse(
    val author: String = "",
    val content: String,
    val id: Long = 0,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val published: Long = 0
)

