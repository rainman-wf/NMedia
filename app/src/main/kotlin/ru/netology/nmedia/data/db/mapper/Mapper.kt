package ru.netology.nmedia.data.db.mapper

import ru.netology.nmedia.data.db.entity.PostEntity
import ru.netology.nmedia.domain.models.Post

fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    dateTime = dateTime,
    isLiked = isLiked,
    likes = likes,
    shares = shares,
    views = views,
    firstUrl = firstUrl
)

fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    dateTime = dateTime,
    isLiked = isLiked,
    likes = likes,
    shares = shares,
    views = views,
    firstUrl = firstUrl
)




