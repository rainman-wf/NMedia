package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long,
    @Embedded(prefix = "author_") val authorEntity: AuthorEntity,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "published") val published: Long,
    @ColumnInfo(name = "liked_by_me") val likedByMe: Boolean = false,
    @ColumnInfo(name = "likes") val likes: Int = 0,
    @ColumnInfo(name = "shares") val shares: Int = 0,
    @ColumnInfo(name = "views") val views: Int = 0,
    @Embedded(prefix = "attachment_") val attachment: AttachmentEntity? = null,
    @ColumnInfo(name = "owned_by_me") val ownedByMe: Boolean
)