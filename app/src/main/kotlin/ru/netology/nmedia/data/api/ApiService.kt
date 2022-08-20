package ru.netology.nmedia.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.common.constants.MAIN_PATH
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.domain.models.Media
import ru.netology.nmedia.domain.models.Post

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST(MAIN_PATH)
    suspend fun send(@Body postRequestBody: PostRequestBody) : Response<Post>

    @GET(MAIN_PATH)
    suspend fun getAll(): Response<List<Post>>

    @POST("$MAIN_PATH/{id}/likes")
    suspend fun like(@Path("id") id: Long): Response<Post>

    @DELETE("$MAIN_PATH/{id}/likes")
    suspend fun unlike(@Path("id") id: Long): Response<Post>

    @DELETE("$MAIN_PATH/{id}")
    suspend fun remove(@Path("id") id: Long): Response<Unit>

    @GET("$MAIN_PATH/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @GET("$MAIN_PATH/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @Multipart
    @POST("$MAIN_PATH/media")
    suspend fun uploadMedia(@Part media: MultipartBody.Part): Response<Media>

}



