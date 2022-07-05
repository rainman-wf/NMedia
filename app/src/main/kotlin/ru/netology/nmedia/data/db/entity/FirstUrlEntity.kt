package ru.netology.nmedia.data.db.entity

import androidx.room.Embedded

data class FirstUrlEntity(
    val url: String,
    @Embedded  val thumbData: ThumbDataEntity? = null
)