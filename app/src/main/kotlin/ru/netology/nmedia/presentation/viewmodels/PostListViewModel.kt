package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.FeedModelState
import ru.netology.nmedia.domain.usecase.*
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    val modelsLiveData: ModelsLiveData,
    val trySendPostUseCase: TrySendPostUseCase,
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase,
    val syncDataUseCase: SyncDataUseCase,
    getNewerUseCase: GetNewerUseCase,
    val setReadUseCase: SetReadUseCase
) : ViewModel() {

//    init {
//        modelsLiveData.state.postValue(FeedModelState(loading = true))
//        getNewerUseCase()
//        syncData()
//    }

    fun setRead(key: Long) {
        viewModelScope.launch {
            setReadUseCase(key)
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
                syncDataUseCase()
                modelsLiveData.state.value = FeedModelState()
            } catch (e: Exception) {
                modelsLiveData.state.value = FeedModelState(error = true)
            }
        }
    }

    fun onTryClicked(key: Long) {
        viewModelScope.launch {
            try {
                trySendPostUseCase(key)
            } catch (e: Exception) {
                log(e.message.toString())
            }
        }
    }

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
}