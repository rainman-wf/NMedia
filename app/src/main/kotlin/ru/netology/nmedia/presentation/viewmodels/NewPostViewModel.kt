package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.usecase.container.NewPostUseCaseContainer

class NewPostViewModel(
    private val liveData: LiveData,
    private val newPostUseCaseContainer: NewPostUseCaseContainer
) : ViewModel() {

    fun onSaveClicked(key: Long, content: String) {
        viewModelScope.launch {
            try {
                newPostUseCaseContainer.savePostUseCase(key, content)
            } catch (e: Exception) {}
        }
    }
}