package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.usecase.SavePostUseCase
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    val savePostUseCase: SavePostUseCase,
    val liveData: ModelsLiveData
) : ViewModel() {

    fun onSaveClicked(key: Long, content: String) {
        viewModelScope.launch {
            try {
                savePostUseCase(
                    key,
                    content,
                    liveData.photo.value?.let { Attachment(it.uri, Attachment.Type.IMAGE) }
                )
            } catch (e: Exception) {
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