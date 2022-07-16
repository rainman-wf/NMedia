package ru.netology.nmedia.data.api.dto

data class UpdatePostContentRequestBody(
    val id: Long,
    val author: String = "",
    val content: String
) : BaseRequest