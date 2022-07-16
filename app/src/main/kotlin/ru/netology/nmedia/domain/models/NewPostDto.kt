package ru.netology.nmedia.domain.models

data class NewPostDto(
    val content: String,
    val attachment: Attachment? = null
)
