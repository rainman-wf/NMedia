package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.usecase.*
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    val modelsLiveData: ModelsLiveData,
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase,
    val savePostUseCase: SavePostUseCase
) : ViewModel() {

    fun onLikeClicked(key: Long) {
        viewModelScope.launch {
            try {
                likePostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }

    fun onRemoveClicked(key: Long) {
        viewModelScope.launch {
            try {
                removePostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }

    fun sendPost(id: Long, content: String) {
        viewModelScope.launch {
            savePostUseCase(
                id,
                content,
                modelsLiveData.photo.value?.let { Attachment(it.uri, Attachment.Type.IMAGE) }
            )
            modelsLiveData.photo.postValue(null)
            modelsLiveData.postSent.postValue(Unit)
        }
    }
}