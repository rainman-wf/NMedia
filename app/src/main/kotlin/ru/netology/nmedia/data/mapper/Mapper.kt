package ru.netology.nmedia.data.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.constants.AUTHOR_AVATAR
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.local.entity.AttachmentEntity
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel
import java.util.*

fun PostEntity.toPost(): Post {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toPost(this)
}

fun PostEntity.toModel() = PostModel(
    key = key,
    post = toPost(),
    state = state,
    read = read
)

fun Post.toEntity(key: Long, synced: Boolean, state: PostModel.State, read: Boolean = true): PostEntity {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toEntity(key, this, synced, state, read)
}

fun Post.toModel(key: Long) = PostModel(
    key = key,
    post = this
)


fun PostEntity.toRequestBody() = PostRequestBody(
    id = id,
    author = author,
    authorAvatar = authorAvatar,
    content = content
)

fun NewPostDto.toEntity() = PostEntity(
    author = AUTHOR,
    authorAvatar = AUTHOR_AVATAR,
    content = content,
    published = Date().time / 1000,
    synced = false,
    state = PostModel.State.LOADING,
)


@Mapper
interface PostConverter {

    fun toPost(postEntity: PostEntity): Post

    @Mapping(target = "key", source = "key")
    @Mapping(target = "synced", source = "synced")
    @Mapping(target = "removed", ignore = true)
    @Mapping(target = "state", source = "state")
    @Mapping(target = "read", source = "read")
    fun toEntity(key: Long, post: Post, synced: Boolean, state: PostModel.State, read: Boolean): PostEntity

}





