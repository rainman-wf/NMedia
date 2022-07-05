package ru.netology.nmedia.domain.models

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val statusEmpty: Boolean = false,
    val statusLoading: Boolean = false,
    val statusSuccess: Boolean = false,
    val statusError: Boolean = false,
    val errorMsg: String? = null
)
