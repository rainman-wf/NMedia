package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo

data class AuthorEntity(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar") val avatar: String? = null
)
