package ru.netology.nmedia.data.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.constants.AUTHOR_AVATAR
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
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

fun NewPostDto.toRequestBody() = PostRequestBody (
    author = AUTHOR,
    authorAvatar = AUTHOR_AVATAR,
    content = content,
    attachment = attachment
)

fun UpdatePostDto.toRequestBody() = PostRequestBody (
    id = id,
    content = content
)

fun UnsentPostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published
)

@Mapper
interface PostConverter {
    fun toPost(postEntity: PostEntity): Post

    @Mapping(target = "syncStatus", source = "synced")
    @Mapping(target = "removed", ignore = true)
    fun toEntity(post: Post, synced: Boolean): PostEntity

    @Mapping(target = "author", ignore = true, defaultValue = AUTHOR)
    @Mapping(target = "authorAvatar", ignore = true, defaultValue = AUTHOR_AVATAR)
    fun toRequestBody(newPostDto: NewPostDto) : PostRequestBody

    fun toRequestBody(updatePostDto: UpdatePostDto) : PostRequestBody
}





