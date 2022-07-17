package ru.netology.nmedia.domain.models

data class PostModel(
    val post: Post,
    val statusLoading: Boolean = false,
    val statusError: Boolean = false
)
