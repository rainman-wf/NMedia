package ru.netology.nmedia.data.api.dto

import ru.netology.nmedia.domain.models.Attachment

data class PostResponseBody(
    val attachment: Attachment?,
    val author: String,
    val authorAvatar: String,
    val authorId: Long,
    val content: String,
    val id: Long,
    val likedByMe: Boolean,
    val likes: Int,
    val published: Long
)