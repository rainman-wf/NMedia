package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.domain.usecase.*

class NewPostViewModelFactory(
    private val updatePostContentUseCase: UpdatePostContentUseCase,
    private val addNewPostUseCase: AddNewPostUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewPostViewModel(
            updatePostContentUseCase,
            addNewPostUseCase,
            getPostByIdUseCase,
        ) as T

    }
}