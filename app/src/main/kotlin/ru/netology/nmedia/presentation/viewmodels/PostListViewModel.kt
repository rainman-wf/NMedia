package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.FeedModelState
import ru.netology.nmedia.domain.usecase.container.PostListUseCaseContainer

class PostListViewModel(
    val modelsLiveData: ModelsLiveData,
    private val postListUseCaseContainer: PostListUseCaseContainer
) : ViewModel() {

    val newCount: LiveData<Int> = postListUseCaseContainer.getNewerUseCase.invoke()

    init {
        modelsLiveData.state.postValue(FeedModelState(loading = true))
        syncData()
    }

    fun setRead(key: Long) {
        viewModelScope.launch {
            postListUseCaseContainer.setReadUseCase(key)
        }
    }

    fun onRefreshSwiped() {
        viewModelScope.launch {
            modelsLiveData.state.postValue(FeedModelState(refreshing = true))
            syncData()
            modelsLiveData.state.value = FeedModelState()
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            try {
                postListUseCaseContainer.syncDataUseCase()
                modelsLiveData.state.value = FeedModelState()
            } catch (e: Exception) {
                modelsLiveData.state.value = FeedModelState(error = true)
            }
        }
    }

    fun onTryClicked(key: Long) {
        viewModelScope.launch {
            try {
                postListUseCaseContainer.trySendPostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }

    fun onLikeClicked(key: Long) {
        viewModelScope.launch {
            try {
                postListUseCaseContainer.likePostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }

    fun onRemoveClicked(key: Long) {
        viewModelScope.launch {
            try {
                postListUseCaseContainer.removePostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }
}