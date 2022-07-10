package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.interactor.PostListInteractor

class PostListViewModelFactory(val liveData: PostsLiveData, private val postListInteractor: PostListInteractor) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(liveData, postListInteractor) as T
    }
}