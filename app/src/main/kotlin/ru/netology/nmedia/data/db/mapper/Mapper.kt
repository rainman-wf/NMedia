package ru.netology.nmedia.data.db.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.netology.nmedia.data.db.entity.PostEntity
import ru.netology.nmedia.data.db.entity.ThumbDataEntity
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.ThumbData

fun PostEntity.toModel(): Post {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toPost(this)
}

fun Post.toEntity(): PostEntity {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toEntity(this)
}

fun ThumbDataEntity.toThumbData() : ThumbData {
    val converter = Mappers.getMapper(PostConverter::class.java)
    return converter.toThumbData(this)
}

@Mapper
interface PostConverter {
    fun toPost(postEntity: PostEntity): Post
    fun toEntity(post: Post): PostEntity
    fun toThumbData(thumbDataEntity: ThumbDataEntity): ThumbData
}





