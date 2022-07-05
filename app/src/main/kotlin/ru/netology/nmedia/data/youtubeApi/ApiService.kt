package ru.netology.nmedia.data.youtubeApi

import retrofit2.Call
import retrofit2.http.*
import ru.netology.nmedia.data.db.entity.ThumbDataEntity
import ru.netology.nmedia.domain.models.ThumbData

interface ApiService {

    @GET("oembed")
    fun getThumbDataEntity(
        @Query("url") url: String,
        @Query("format") format: String = "json"
    ): Call<ThumbDataEntity>
}