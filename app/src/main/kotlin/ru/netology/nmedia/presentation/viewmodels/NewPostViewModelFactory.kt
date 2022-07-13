package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.interactor.NewPostInteractor

class NewPostViewModelFactory(
    private val liveData: PostsLiveData,
    private val newPostInteractor: NewPostInteractor
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewPostViewModel(liveData, newPostInteractor) as T

    }
}