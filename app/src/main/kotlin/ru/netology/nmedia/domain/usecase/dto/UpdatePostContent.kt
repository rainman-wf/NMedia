package ru.netology.nmedia.domain.usecase.dto

data class UpdatePostContent(
    val id: Long,
    val content: String,
    val url: String? = null
)