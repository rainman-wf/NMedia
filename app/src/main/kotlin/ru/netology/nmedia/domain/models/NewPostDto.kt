package ru.netology.nmedia.domain.models

data class NewPostDto(
    private val author: String = "",
    val content: String,
    val attachment: Attachment? = null
)
