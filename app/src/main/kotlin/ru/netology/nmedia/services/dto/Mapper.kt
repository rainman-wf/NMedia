package ru.netology.nmedia.services.dto

import ru.netology.nmedia.domain.models.Post

fun NewPostInfo.toPost() : Post {
    return Post(
        id = id,
        author = author,
        content = this.content,
        dateTime = published,
    )
}