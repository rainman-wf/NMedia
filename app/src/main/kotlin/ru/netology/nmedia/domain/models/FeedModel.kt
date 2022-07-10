package ru.netology.nmedia.domain.models

data class FeedModel(
    val posts: MutableMap<Long, PostModel> = mutableMapOf(),
    val statusEmpty: Boolean = false,
    val statusLoading: Boolean = false,
    val statusUpdating: Boolean = false,
    val statusSuccess: Boolean = false,
    val statusError: Boolean = false
)
