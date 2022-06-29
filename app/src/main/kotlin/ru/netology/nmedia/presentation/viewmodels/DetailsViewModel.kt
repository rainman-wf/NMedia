package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.usecase.*

class DetailsViewModel(
    private val likePostUseCase: LikePostUseCase,
    private val sharePostUseCase: SharePostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val getObservableByIdUseCase: GetObservableByIdUseCase
): ViewModel() {

    fun onLikeClicked(id: Long) {
        likePostUseCase.invoke(id)
    }

    fun onShareClicked(id: Long) {
        sharePostUseCase.invoke(id)
    }

    fun onRemoveClicked(id: Long) {
        removePostUseCase.invoke(id)
    }

    fun observable(id: Long) = getObservableByIdUseCase.invoke(id)
}