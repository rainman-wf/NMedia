package ru.netology.nmedia.data

import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.data.api.dto.PushToken
import ru.netology.nmedia.domain.models.UploadMediaDto
import ru.netology.nmedia.domain.repository.AuthManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManagerImpl @Inject constructor(
    prefs: SharedPreferences,
    private val api: ApiService,
) : AuthManager {

    private val appAuth = AppAuth.getInstance()

    init {
        val id = prefs.getLong(appAuth.idKey, 0L)
        val token = prefs.getString(appAuth.tokenKey, null)

        if (id != 0L && token != null)
            appAuth.setAuth(id, token)

        sendPushToken()
    }

    override suspend fun login(login: String, password: String) {
        val response = api.login(login, password)
        val responseBody =
            if (response.isSuccessful) response.body() ?: error("body is null")
            else throw ApiError(response.code(), response.message())

        appAuth.setAuth(responseBody.id, responseBody.token)
        sendPushToken()
    }

    override suspend fun simpleRegister(login: String, password: String, username: String) {
        val response = api.simpleRegister(login, password, username)
        val responseBody =
            if (response.isSuccessful) response.body() ?: error("body is null")
            else throw ApiError(response.code(), response.message())

        appAuth.setAuth(responseBody.id, responseBody.token)
        sendPushToken()
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
        sendPushToken()
    }

    override fun getId(): Long {
        return appAuth.authStateFlow.value.id
    }

    override fun logOut() {
        appAuth.removeAuth()
        sendPushToken()
    }

    override fun sendPushToken(token: String?) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                log(pushToken)
                api.saveToken(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}