package ru.netology.nmedia.data.remote

import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.data.remote.dto.PostResponse

class RemoteDataSource(private val client: OkHttpClient) {

    private val gson = Gson()

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private const val PATH = "/api/slow/posts/"
    }

    fun send(content: String, callback: Callback) {
        val request: Request = Request.Builder()
            .header("Content-Type", "application/json")
            .post(
                gson.toJson(
                    PostResponse(
                        author = AUTHOR,
                        content = content
                    )
                ).toRequestBody()
            )
            .url("$BASE_URL$PATH")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun updateContent(id: Long, content: String, callback: Callback) {
        val request: Request = Request.Builder()
            .header("Content-Type", "application/json")
            .post(
                gson.toJson(
                    PostResponse(
                        id = id,
                        content = content
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
            .url("$BASE_URL$PATH$id/likes")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun unlike(id: Long, callback: Callback) {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL$PATH$id/likes")
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun getAll(callback: Callback) {
        val request: Request = Request.Builder()
            .url("$BASE_URL$PATH")
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