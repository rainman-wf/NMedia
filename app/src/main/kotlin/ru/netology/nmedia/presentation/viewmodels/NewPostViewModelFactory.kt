package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.container.NewPostUseCaseContainer

class NewPostViewModelFactory(
    private val newPostUseCaseContainer: NewPostUseCaseContainer,
    private val liveData: ModelsLiveData
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewPostViewModel(newPostUseCaseContainer, liveData) as T

    }
}