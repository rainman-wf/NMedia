package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsentPosts")
data class UnsentPostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "authorAvatar") val authorAvatar: String? = null,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "published") val published: Long,
    @Embedded(prefix = "attachment_") val attachment: AttachmentEntity? = null
)
