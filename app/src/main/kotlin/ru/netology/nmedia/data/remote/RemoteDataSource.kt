package ru.netology.nmedia.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.data.PostsRepositoryImpl
import ru.netology.nmedia.data.remote.dto.PostResponse
import ru.netology.nmedia.data.remote.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.params.NewPostParam
import ru.netology.nmedia.domain.usecase.params.UpdateCurrentPostParam
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

class RemoteDataSource(private val client: OkHttpClient) {

    private val gson = Gson()

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private const val PATH = "/api/slow/posts/"
    }

    fun send(newPostParam: NewPostParam, callback: Callback) {
        val request: Request = Request.Builder()
            .header("Content-Type", "application/json")
            .post(
                gson.toJson(
                    PostResponse(
                        author = newPostParam.author,
                        content = newPostParam.content
                    )
                ).toRequestBody()
            )
            .url("$BASE_URL$PATH")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun updateContent(updateCurrentPostParam: UpdateCurrentPostParam, callback: Callback) {
        val request: Request = Request.Builder()
            .header("Content-Type", "application/json")
            .post(
                gson.toJson(
                    PostResponse(
                        id = updateCurrentPostParam.id,
                        content = updateCurrentPostParam.content
                    )
                ).toRequestBody()
            )
            .url("$BASE_URL$PATH")
            .build()

        client.newCall(request).enqueue(callback)
    }


    fun like(id: Long, callback: Callback) {
        val request: Request = Request.Builder()
            .post("{}".toRequestBody())
            .url("${BASE_URL}${PATH}${id}/likes")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun unlike(id: Long, callback: Callback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}${PATH}${id}/likes")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun getAll(callback: Callback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}${PATH}")
            .build()

        client.newCall(request).enqueue(callback)
    }


    fun remove(id: Long, callback: Callback) {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL$PATH$id")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun getById(id: Long, callback: Callback) {
        val request: Request = Request.Builder()
            .url("$BASE_URL$PATH$id")
            .build()

        client.newCall(request).enqueue(callback)
    }
}