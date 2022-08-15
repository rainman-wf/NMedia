package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.container.PostListUseCaseContainer

class PostListViewModelFactory(private val modelsLiveData: ModelsLiveData, private val postListUseCaseContainer: PostListUseCaseContainer) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(modelsLiveData, postListUseCaseContainer) as T
    }
}