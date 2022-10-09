package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.models.UploadMediaDto
import ru.netology.nmedia.domain.repository.AuthManager
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val liveData: ModelsLiveData,
    private val authManager: AuthManager,
) : ViewModel() {

    val errorEvent = SingleLiveEvent<String>()
    val okEvent = SingleLiveEvent<Unit>()

    fun changePhoto(uri: Uri) {
        liveData.photo.value = PhotoModel(uri.toString())
    }

    private fun clearPhoto() {
        liveData.photo.value = null
    }

    fun signUp(login: String, password: String, username: String) {
        viewModelScope.launch {
            try {
                liveData.photo.value?.uri?.toUri()?.toFile()?.let {
                    authManager.register(login, password, username, UploadMediaDto(it))
                } ?: authManager.simpleRegister(login, password, username)
                okEvent.postValue(Unit)
                clearPhoto()
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
                authManager.login(login, password)
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