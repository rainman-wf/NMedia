package ru.netology.nmedia.domain.models

data class Author(
    val id: Long,
    val name: String,
    val avatar: String? = null
)
