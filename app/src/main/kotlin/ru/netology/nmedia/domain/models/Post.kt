package ru.netology.nmedia.domain.models

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val dateTime: Long,
    val isLiked: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val firstUrl: FirstUrl? = null
)
