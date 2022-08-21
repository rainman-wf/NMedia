package ru.netology.nmedia.domain.models

data class FeedModel(
    val posts: MutableMap<Long, PostModel> = mutableMapOf()
)

