package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.container.PostDetailsUseCaseContainer

class DetailsViewModelFactory(
    private val modelsLiveData: ModelsLiveData,
    private val postDetailsUseCaseContainer: PostDetailsUseCaseContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(modelsLiveData, postDetailsUseCaseContainer) as T
    }
}