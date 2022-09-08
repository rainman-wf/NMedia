package ru.netology.nmedia.data

import android.content.SharedPreferences
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.api.dto.Login
import ru.netology.nmedia.data.api.dto.Username
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.domain.models.UploadMediaDto
import ru.netology.nmedia.domain.repository.AuthService

class AuthServiceImpl(
    prefs: SharedPreferences,
    private val api: ApiService,
) : AuthService {

    private val appAuth = AppAuth.getInstance()

    companion object {
        const val ID_KEY = "id_key"
        const val TOKEN_KEY = "token_key"
    }

    init {

        val id = prefs.getLong(ID_KEY, 0L)
        val token = prefs.getString(TOKEN_KEY, null)

        if (id != 0L && token != null)
            appAuth.setAuth(id, token)
    }

    override suspend fun login(login: String, password: String) {
        val response = api.login(login, password)
        val responseBody =
            if (response.isSuccessful) response.body() ?: error("body is null")
            else throw ApiError(response.code(), response.message())

        appAuth.setAuth(responseBody.id, responseBody.token)
    }

    override suspend fun simpleRegister(login: String, password: String, username: String) {
        val response = api.simpleRegister(login, password, username)
        val responseBody =
            if (response.isSuccessful) response.body() ?: error("body is null")
            else throw ApiError(response.code(), response.message())

        appAuth.setAuth(responseBody.id, responseBody.token)
    }

    override suspend fun register(
        login: String,
        password: String,
        username: String,
        avatar: UploadMediaDto
    ) {
        val response = api.register(
            login = login.toRequestBody("text/plane".toMediaType()),
            password = password.toRequestBody("text/plane".toMediaType()),
            name = username.toRequestBody("text/plane".toMediaType()),
            MultipartBody.Part.createFormData("file", avatar.file.name, avatar.file.asRequestBody())
        )

        val responseBody =
            if (response.isSuccessful) response.body() ?: error("body is null")
            else {
                throw ApiError(response.code(), response.message())
            }

        appAuth.setAuth(responseBody.id, responseBody.token)
    }

    override fun logOut() {
        appAuth.removeAuth()
    }

}