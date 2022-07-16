package ru.netology.nmedia.data.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.constants.AUTHOR_AVATAR
import ru.netology.nmedia.common.constants.UNSENT_POST_ID_OFFSET
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.data.api.dto.NewPostRequestBody
import ru.netology.nmedia.data.api.dto.UpdatePostContentRequestBody
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.UpdatePostDto

fun PostEntity.toModel(): Post {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toPost(this)
}

fun Post.toEntity(synced: Boolean): PostEntity {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toEntity(this, synced)
}

fun NewPostDto.toRequestBody() = NewPostRequestBody (
    author = AUTHOR,
    authorAvatar = AUTHOR_AVATAR,
    content = content,
    attachment = attachment
)

fun UpdatePostDto.toRequestBody() = UpdatePostContentRequestBody (
    id = id,
    author = "",
    content = content
)

fun UnsentPostEntity.toModel() = Post(
    id = id + UNSENT_POST_ID_OFFSET,
    author = author,
    content = content,
    dateTime = published
)

@Mapper
interface PostConverter {
    @Mapping(target = "isLiked", source = "liked")
    fun toPost(postEntity: PostEntity): Post

    @Mapping(target = "syncStatus", source = "synced")
    @Mapping(target = "isLiked", source = "post.liked")
    fun toEntity(post: Post, synced: Boolean): PostEntity
}





