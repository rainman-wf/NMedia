package ru.netology.nmedia.data.api.dto

import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.domain.models.Attachment

data class PostRequestBody(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("author") val author: String,
    @SerializedName("authorAvatar") val authorAvatar: String? = null,
    @SerializedName("content") val content: String,
    @SerializedName("attachment") val attachment: Attachment? = null
)

