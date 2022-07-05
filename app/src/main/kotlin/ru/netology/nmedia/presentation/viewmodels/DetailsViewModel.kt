package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.usecase.*
import kotlin.concurrent.thread

class DetailsViewModel(
    private val likePostUseCase: LikePostUseCase,
    private val sharePostUseCase: SharePostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getObservableByIdUseCase: GetObservableByIdUseCase
): ViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
    get() = _post

    fun loadPost(id: Long) {
        thread {
            _post.postValue(getPostByIdUseCase.invoke(id))
        }
    }

    fun onLikeClicked(id: Long) {
        Thread {
            try {
                likePostUseCase.invoke(id)
                loadPost(id)
            } catch (e: Exception) {
                TODO()
            }
        }.start()
    }

    fun onShareClicked(id: Long) {
        sharePostUseCase.invoke(id)
    }

    fun onRemoveClicked(id: Long) {
        Thread {
            try {
                removePostUseCase.invoke(id)
                loadPost(id)
            } catch (e: Exception) {
                TODO()
            }
        }.start()
    }

    fun observable(id: Long) = getObservableByIdUseCase.invoke(id)
}