package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.interactor.NewPostInteractor
import ru.netology.nmedia.domain.usecase.params.NewPostParam

class NewPostViewModel(
    private val liveData: PostsLiveData,
    private val newPostInteractor: NewPostInteractor
) : ViewModel() {

    fun send(author: String, content: String) {

        val newPostParam = NewPostParam(author, content)
        val post = newPostInteractor.saveWorkpieceUseCase.invoke(newPostParam)

        liveData.insert(post.id, PostModel(post, statusLoading = true))

        newPostInteractor.sendPostUseCase.invoke(
            newPostParam,
            object : PostRepository.Callback<Post> {
                override fun onSuccess(data: Post) {

                    newPostInteractor.removeWorkpieceUseCase.invoke(post.id - 2000000000)
                    liveData.replace(post.id, data.id, PostModel(data))
                }

                override fun onFailure(e: Exception) {
                    liveData.insert(post.id, PostModel(post, statusError = true))
                }
            })
    }
}