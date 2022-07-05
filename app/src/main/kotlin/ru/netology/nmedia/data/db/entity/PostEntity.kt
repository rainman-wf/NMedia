package ru.netology.nmedia.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "dateTime") val dateTime: Long = Date().time,
    @ColumnInfo(name = "isLiked") val isLiked: Boolean = false,
    @ColumnInfo(name = "likes") val likes: Int = 0,
    @ColumnInfo(name = "shares") val shares: Int = 0,
    @ColumnInfo(name = "views") val views: Int = 0,
    @Embedded val firstUrl: FirstUrlEntity? = null,
)