package ru.netology.nmedia.domain.models

data class TimeSeparator(
    override val id: Long,
    val time: String
) : FeedItem
