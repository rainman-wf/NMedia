package ru.netology.nmedia.domain.models

data class Post(
    override val id: Long,
    val author: Author,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean
) : FeedItem


