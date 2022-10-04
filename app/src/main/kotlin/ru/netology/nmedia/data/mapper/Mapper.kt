package ru.netology.nmedia.data.mapper

import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.api.dto.PostResponseBody
import ru.netology.nmedia.data.local.entity.AttachmentEntity
import ru.netology.nmedia.data.local.entity.AuthorEntity
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.domain.models.*
import java.util.*

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

fun PostEntity.toModel() = PostModel(
    key = key,
    post = toPost(),
    state = state,
    read = read
)

fun PostResponseBody.toEntity(key: Long, synced: Boolean, state: PostModel.State, read: Boolean = true) =
    PostEntity(
        key = key,
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
        state = state,
        read = read,
        synced = synced,
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


fun Post.toModel(key: Long) = PostModel(
    key = key,
    post = this
)

fun PostResponseBody.toModel() =
    PostModel(
        key = id,
        state = PostModel.State.OK,
        post = toPost()
    )


fun PostEntity.toRequestBody() = PostRequestBody(
    id = id,
    author = authorEntity.name,
    authorAvatar = authorEntity.avatar,
    content = content
)

fun NewPostDto.toEntity() = PostEntity(
    authorEntity = author.toEntity(),
    content = content,
    published = Date().time / 1000,
    synced = false,
    state = PostModel.State.LOADING,
    attachment = attachment?.toEntity(),
    ownedByMe = true
)

fun Author.toEntity() = AuthorEntity(
    id = id,
    name = name,
    avatar = avatar
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





