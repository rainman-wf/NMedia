package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.usecase.container.NewPostUseCaseContainer
import java.io.File

class NewPostViewModel(
    private val newPostUseCaseContainer: NewPostUseCaseContainer,
    private val liveData: ModelsLiveData
) : ViewModel() {

    fun onSaveClicked(key: Long, content: String) {
        viewModelScope.launch {
            try {
                newPostUseCaseContainer.savePostUseCase(key, content, liveData.photo.value?.let { Attachment(it.uri.toString(), Attachment.Type.IMAGE) })
            } catch (e: Exception) {
            }
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        liveData.photo.postValue(PhotoModel(uri, file))
    }
}