package ru.netology.nmedia.data.api.dto

import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.domain.models.Attachment

data class NewPostRequestBody(
    @SerializedName("author") val author: String,
    @SerializedName("authorAvatar") val authorAvatar: String,
    @SerializedName("content") val content: String,
    @SerializedName("attachment") val attachment: Attachment? = null
) : BaseRequest