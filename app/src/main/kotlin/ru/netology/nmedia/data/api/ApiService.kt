package ru.netology.nmedia.data.api

import retrofit2.Call
import retrofit2.http.*
import ru.netology.nmedia.common.constants.MAIN_PATH
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.domain.models.Post

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/posts")
    fun send(@Body postRequestBody: PostRequestBody) : Call<Post>

    @GET(MAIN_PATH)
    fun getAll(): Call<List<Post>>

    @POST("$MAIN_PATH/{id}/likes")
    fun like(@Path("id") id: Long): Call<Post>

    @DELETE("$MAIN_PATH/{id}/likes")
    fun unlike(@Path("id") id: Long): Call<Post>

    @DELETE("$MAIN_PATH/{id}")
    fun remove(@Path("id") id: Long): Call<Unit>

    @GET("$MAIN_PATH/{id}")
    fun getById(@Path("id") id: Long): Call<Post>
}



