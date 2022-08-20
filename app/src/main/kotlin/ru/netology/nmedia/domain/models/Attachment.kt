package ru.netology.nmedia.domain.models

data class Attachment(
    val url: String,
    val type: Type
) {
    enum class Type {
        IMAGE
    }
}