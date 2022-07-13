package ru.netology.nmedia.data.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.common.constants.UNSENT_POST_ID_OFFSET
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.data.remote.dto.PostResponse
import ru.netology.nmedia.domain.models.Post

fun PostEntity.toModel(): Post {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toPost(this)
}

fun Post.toEntity(synced: Boolean): PostEntity {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toEntity(this, synced)
}

fun UnsentPostEntity.toModel() = Post(
    id = id + UNSENT_POST_ID_OFFSET,
    author = author,
    content = content,
    dateTime = published
)

fun PostResponse.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    dateTime = published * 1000,
    isLiked = likedByMe,
    likes = likes,
    syncStatus = true
)

fun PostResponse.toModel() = Post(
    id = id,
    author = author,
    content = content,
    dateTime = published * 1000,
    isLiked = likedByMe,
    likes = likes,
)

@Mapper
interface PostConverter {
    @Mapping(target = "isLiked", source = "liked")
    fun toPost(postEntity: PostEntity): Post

    @Mapping(target = "syncStatus", source = "synced")
    fun toEntity(post: Post, synced: Boolean): PostEntity
}





