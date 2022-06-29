package ru.netology.nmedia.services.dto

import ru.netology.nmedia.domain.models.FirstUrl
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.ThumbData

fun NewPostInfo.toPost() : Post {
    return Post(
        id = id,
        author = author,
        content = this.content,
        dateTime = published,
        firstUrl = firstUrl?.let {
            FirstUrl(
                url = it,
                thumbData = ThumbData(
                    thumbnail_width!!,
                    thumbnail_height!!,
                    thumbnail_url!!
                )
            )
        }
    )
}