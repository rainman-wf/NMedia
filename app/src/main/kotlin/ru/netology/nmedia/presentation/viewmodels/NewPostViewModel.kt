package ru.netology.nmedia.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.models.PhotoModel
import ru.netology.nmedia.domain.usecase.SavePostUseCase
import ru.netology.nmedia.domain.usecase.TrySendPostUseCase
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    val savePostUseCase: SavePostUseCase,
    val liveData: ModelsLiveData,
    val trySendPostUseCase: TrySendPostUseCase
) : ViewModel() {

    fun onSaveClicked(key: Long, content: String) {
        viewModelScope.launch {
            try {
                val resultKey = savePostUseCase(
                    key,
                    content,
                    liveData.photo.value?.let { Attachment(it.uri, Attachment.Type.IMAGE) }
                )
                liveData.postCreated.postValue(resultKey)
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