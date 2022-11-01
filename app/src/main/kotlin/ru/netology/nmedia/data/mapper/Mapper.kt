package ru.netology.nmedia.data.mapper

import ru.netology.nmedia.data.api.dto.PostResponseBody
import ru.netology.nmedia.data.local.entity.AttachmentEntity
import ru.netology.nmedia.data.local.entity.AuthorEntity
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.domain.models.*

fun PostEntity.toPost() = Post(
    id = id,
    author = authorEntity.toModel(),
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    shares = shares,
    views = views,
    attachment = attachment?.toModel(),
    ownedByMe = ownedByMe
)


fun PostResponseBody.toEntity() =
    PostEntity(
        id = id,
        authorEntity = AuthorEntity(
            authorId,
            author,
            authorAvatar
        ),
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        attachment = attachment?.toEntity(),
        ownedByMe = ownedByMe
    )

fun PostResponseBody.toPost() =
    Post(
        id = id,
        author = Author(
            authorId,
            author,
            authorAvatar
        ),
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        attachment = attachment,
        ownedByMe = ownedByMe
    )

fun AuthorEntity.toModel() = Author(
    id = id,
    name = name,
    avatar = avatar
)

fun Attachment.toEntity(): AttachmentEntity {
    return AttachmentEntity(
        url = url,
        type = type
    )
}

fun AttachmentEntity.toModel(): Attachment {
    return Attachment(
        url = url,
        type = type
    )
}





