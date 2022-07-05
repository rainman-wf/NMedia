package ru.netology.nmedia.data.db.entity

import com.google.gson.annotations.SerializedName

data class ThumbDataEntity (
    @SerializedName("thumbnail_width")
    val thumbnailWidth: Int,
    @SerializedName("thumbnail_height")
    val thumbnailHeight: Int,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String
)