package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo

data class AttachmentEntity(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "type") val type: String
)