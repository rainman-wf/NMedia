package ru.netology.nmedia.domain.usecase.dto

data class NewPost(
    val author: String,
    val content: String,
    val url: String? = null
)