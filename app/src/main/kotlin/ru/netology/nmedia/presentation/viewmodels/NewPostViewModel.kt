package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.models.UpdatePostDto
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    val liveData: ModelsLiveData,
) : ViewModel() {

    fun onSaveClicked(id: Long, content: String) {
        viewModelScope.launch {
            try {
                liveData.postCreated.postValue(
                    UpdatePostDto(
                        id,
                        content = content
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changePhoto(uri: Uri) {
        liveData.photo.value = PhotoModel(uri.toString())
    }

    fun clearAttachment() {
        liveData.photo.value = null
    }
}