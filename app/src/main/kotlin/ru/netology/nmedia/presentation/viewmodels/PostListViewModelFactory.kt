package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.*

class PostListViewModelFactory(
    private val likePostUseCase: LikePostUseCase,
    private val sharePostUseCase: SharePostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val getPostsListUseCase: GetPostsListUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostListViewModel(
            likePostUseCase,
            sharePostUseCase,
            removePostUseCase,
            getPostsListUseCase
        ) as T
    }
}