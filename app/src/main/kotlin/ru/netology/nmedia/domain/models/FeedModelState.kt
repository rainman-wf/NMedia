package ru.netology.nmedia.domain.models

data class FeedModelState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val error: Boolean = false
)