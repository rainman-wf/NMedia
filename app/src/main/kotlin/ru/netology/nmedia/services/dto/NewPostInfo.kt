package ru.netology.nmedia.services.dto

import ru.netology.nmedia.domain.models.Author

data class NewPostInfo(
    val id: Long,
    val author: Author,
    val authorAvatar: String? = null,
    val published: Long,
    val content: String,
    val firstUrl: String? = null,
    val thumbnail_url: String? = null,
    val thumbnail_width: Int? = null,
    val thumbnail_height: Int? = null
)