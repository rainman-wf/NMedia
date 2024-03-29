package ru.netology.nmedia.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.data.api.dto.LoginResponse
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.api.dto.PostResponseBody
import ru.netology.nmedia.domain.models.Media
import ru.netology.nmedia.data.api.dto.PushToken

interface ApiService {

    @POST("posts")
    suspend fun send(@Body postRequestBody: PostRequestBody) : Response<PostResponseBody>

    @GET("posts")
    suspend fun getAll(): Response<List<PostResponseBody>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<PostResponseBody>>

    @POST("posts/{id}/likes")
    suspend fun like(@Path("id") id: Long): Response<PostResponseBody>

    @DELETE("posts/{id}/likes")
    suspend fun unlike(@Path("id") id: Long): Response<PostResponseBody>

    @DELETE("posts/{id}")
    suspend fun remove(@Path("id") id: Long): Response<Unit>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<PostResponseBody>>

    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Long, @Query("count") count: Int): Response<List<PostResponseBody>>

    @GET("posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Long, @Query("count") count: Int): Response<List<PostResponseBody>>

    @Multipart
    @POST("media")
    suspend fun uploadMedia(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun login(@Field("login") login: String, @Field("pass") pass: String): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun simpleRegister(
        @Field("login") login: String,
        @Field("pass") password: String,
        @Field("name") name: String
    ): Response<LoginResponse>

    @Multipart
    @POST("users/registration")
    suspend fun register(
        @Part("login") login: RequestBody,
        @Part("pass") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part,
    ): Response<LoginResponse>

    @POST("users/push-tokens")
    suspend fun saveToken(@Body pushToken: PushToken) : Response<Unit>
}



