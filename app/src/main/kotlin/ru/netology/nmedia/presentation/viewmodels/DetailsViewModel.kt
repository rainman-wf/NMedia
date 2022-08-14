package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.domain.usecase.container.PostDetailsUseCaseContainer

class DetailsViewModel(
    val liveData: LiveData,
    private val postDetailsUseCaseContainer: PostDetailsUseCaseContainer
) : ViewModel() {

    fun onLikeClicked (key: Long) {
        viewModelScope.launch {
            postDetailsUseCaseContainer.likePostUseCase(key)
        }
    }

    fun onRemoveClicked (key: Long) {
        viewModelScope.launch {
            postDetailsUseCaseContainer.removePostUseCase(key)
        }
    }
}