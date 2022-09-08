package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.exceptions.AppError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.dto.Password
import ru.netology.nmedia.data.api.dto.Username
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.data.auth.AuthState
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.models.UploadMediaDto
import ru.netology.nmedia.domain.repository.AuthService

class AuthViewModel(
    val liveData: ModelsLiveData,
    private val authService: AuthService,
) : ViewModel() {

    val errorEvent = SingleLiveEvent<String>()
    val okEvent = SingleLiveEvent<Unit>()

    fun changePhoto(uri: Uri) {
        liveData.photo.value = PhotoModel(uri.toString())
    }

    fun clearPhoto() {
        liveData.photo.value = null
    }

    fun signUp(login: String, password: String, username: String) {
        viewModelScope.launch {
            try {
                liveData.photo.value?.uri?.toUri()?.toFile()?.let {
                    authService.register(login, password, username, UploadMediaDto(it))
                } ?: authService.simpleRegister(login, password, username)
                okEvent.postValue(Unit)
            } catch (e: ApiError) {
                when(e.status)  {
                    403 -> errorEvent.postValue("User already exists")
                    else -> errorEvent.postValue("Unexpected error")
                }
            }
        }
    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            try {
                authService.login(login, password)
                okEvent.postValue(Unit)
            } catch (e: ApiError) {
                when(e.status)  {
                    404 -> errorEvent.postValue("User not found")
                    400 -> errorEvent.postValue("Invalid password")
                    else -> errorEvent.postValue("Unexpected error")
                }
            }
        }
    }

}