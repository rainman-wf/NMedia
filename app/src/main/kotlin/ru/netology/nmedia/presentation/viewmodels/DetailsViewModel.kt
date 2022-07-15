package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.usecase.container.PostDetailsUseCaseContainer

class DetailsViewModel(
    val liveData: PostsLiveData,
    private val postDetailsUseCaseContainer: PostDetailsUseCaseContainer
) : ViewModel() {

    fun onLikeClicked (id: Long) {
        liveData.updateItem(postDetailsUseCaseContainer.likePostUseCase(id))
    }

    fun onRemoveClicked (id: Long) {
        postDetailsUseCaseContainer.removePostUseCase(id)
        liveData.removeItem(id)
    }
}