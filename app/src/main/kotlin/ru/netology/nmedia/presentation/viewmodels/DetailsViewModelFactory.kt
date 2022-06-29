package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.*

class DetailsViewModelFactory(
    private val likePostUseCase: LikePostUseCase,
    private val sharePostUseCase: SharePostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val getObservableByIdUseCase: GetObservableByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(
            likePostUseCase,
            sharePostUseCase,
            removePostUseCase,
            getObservableByIdUseCase
        ) as T
    }
}