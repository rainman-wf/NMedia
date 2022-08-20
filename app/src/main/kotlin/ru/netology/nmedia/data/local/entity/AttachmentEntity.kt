package ru.netology.nmedia.data.local.entity

import androidx.room.ColumnInfo
import ru.netology.nmedia.domain.models.Attachment

data class AttachmentEntity(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "type") val type: Attachment.Type
)