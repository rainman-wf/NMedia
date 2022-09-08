package ru.netology.nmedia.domain.models

data class NewPostDto(
    val author: Author,
    val content: String,
    val attachment: Attachment? = null
)
