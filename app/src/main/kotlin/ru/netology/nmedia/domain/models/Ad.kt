package ru.netology.nmedia.domain.models

data class Ad(
    override val id: Long,
    val image: String
) : FeedItem
