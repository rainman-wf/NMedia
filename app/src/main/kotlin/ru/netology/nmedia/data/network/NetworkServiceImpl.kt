package ru.netology.nmedia.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.data.network.dto.PostResponse
import java.lang.RuntimeException

class NetworkServiceImpl(
    private val client: OkHttpClient
) : NetworkService {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<PostResponse>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private const val PATH = "/api/posts/"
    }

    override fun getAll(): List<PostResponse> {
        val request: Request = Request.Builder()
            .url("$BASE_URL$PATH")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, typeToken.type) }
    }

    override fun getByID(id: Long): PostResponse {
        val request: Request = Request.Builder()
            .url("$BASE_URL$PATH$id")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, PostResponse::class.java) }
    }

    override fun save(postResponse: PostResponse): PostResponse {
        val request: Request = Request.Builder()
            .header("Content-Type", "application/json")
            .post(gson.toJson(postResponse).toRequestBody())
            .url("$BASE_URL$PATH")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, PostResponse::class.java) }
    }

    override fun likeById(id: Long): PostResponse {
        val request: Request = Request.Builder()
            .post("{}".toRequestBody())
            .url("$BASE_URL$PATH$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, PostResponse::class.java) }
    }

    override fun unlikeById(id: Long): PostResponse {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL$PATH$id/likes")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, PostResponse::class.java) }
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL$PATH$id")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("null body") }
            .let { gson.fromJson(it, PostResponse::class.java) }
    }
}



