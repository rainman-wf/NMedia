package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.FeedModelState
import ru.netology.nmedia.domain.usecase.container.PostListUseCaseContainer

class PostListViewModel(
    val liveData: LiveData,
    private val postListUseCaseContainer: PostListUseCaseContainer
) : ViewModel() {

    init {
        liveData.state.postValue(FeedModelState(loading = true))
        syncData()
    }

    fun onRefreshSwiped() {
        viewModelScope.launch {
            liveData.state.postValue(FeedModelState(refreshing = true))
            syncData()
            liveData.state.value = FeedModelState()
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            try {
                postListUseCaseContainer.syncDataUseCase()
                liveData.state.value = FeedModelState()
            } catch (e: Exception) {
                liveData.state.value = FeedModelState(error = true)
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