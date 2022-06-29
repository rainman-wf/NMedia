package ru.netology.nmedia.domain.models

import androidx.room.Embedded

data class FirstUrl(
    val url: String,
    @Embedded  val thumbData: ThumbData? = null
)