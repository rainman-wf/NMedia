package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "dateTime") val dateTime: Long,
    @ColumnInfo(name = "isLiked") val isLiked: Boolean = false,
    @ColumnInfo(name = "likes") val likes: Int = 0,
    @ColumnInfo(name = "shares") val shares: Int = 0,
    @ColumnInfo(name = "views") val views: Int = 0,
    @ColumnInfo(name = "syncStatus") val syncStatus: Boolean = true
)