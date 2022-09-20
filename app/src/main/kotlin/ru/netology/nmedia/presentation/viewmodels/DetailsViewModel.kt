package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.domain.usecase.LikePostUseCase
import ru.netology.nmedia.domain.usecase.RemovePostUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val modelsLiveData: ModelsLiveData,
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase
) : ViewModel() {

    fun onLikeClicked (key: Long) {
        viewModelScope.launch {
            likePostUseCase(key)
        }
    }

    fun onRemoveClicked (key: Long) {
        viewModelScope.launch {
            removePostUseCase(key)
        }
    }
}