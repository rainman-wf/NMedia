package ru.netology.nmedia.data.remote.mapper

import ru.netology.nmedia.data.remote.dto.PostResponse
import ru.netology.nmedia.domain.models.Post

fun PostResponse.toModel(): Post {

    return Post(
        id = id,
        author = author,
        content = content,
        dateTime = published * 1000,
        isLiked = likedByMe,
        likes = likes,
    )
}

fun parseUrl(text: String): String? {
    val regexp = "(http|https)://[\\w]*\\.\\S*".toRegex()
    return regexp.find(text)?.groups?.first()?.value
}
