package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.UploadMediaDto

interface AuthManager {

    suspend fun login(login: String, password: String)

    suspend fun simpleRegister(
        login: String,
        password: String,
        username: String
    )

    suspend fun register(
        login: String,
        password: String,
        username: String,
        avatar: UploadMediaDto
    )

    fun getId() : Long
    fun logOut()
    fun sendPushToken(token: String? = null)

}