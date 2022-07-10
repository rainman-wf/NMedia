package ru.netology.nmedia.data.local.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.UnsentPostEntity
import ru.netology.nmedia.domain.models.Post

fun PostEntity.toModel(): Post {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toPost(this)
}

fun Post.toEntity(synced: Boolean): PostEntity {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toEntity(this, synced)
}

fun UnsentPostEntity.toPost() = Post(
    id = id + 2000000000,
    author = author,
    content = content,
    dateTime = published
)

@Mapper
interface PostConverter {
    @Mapping(target = "isLiked", source = "liked")
    fun toPost(postEntity: PostEntity): Post

    @Mapping(target = "syncStatus", source = "synced")
    fun toEntity(post: Post, synced: Boolean): PostEntity
}





