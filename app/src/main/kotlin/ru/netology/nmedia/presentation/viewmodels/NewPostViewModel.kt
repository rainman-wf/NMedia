package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.container.NewPostUseCaseContainer

class NewPostViewModel(
    private val liveData: PostsLiveData,
    private val newPostUseCaseContainer: NewPostUseCaseContainer
) : ViewModel() {

    fun onSaveClicked(id: Long, content: String) {

        val post = newPostUseCaseContainer.savePostUseCase(id, content)

        liveData.insert(post.id, PostModel(post, statusLoading = true))

        newPostUseCaseContainer.sendPostUseCase(
            content,
            object : PostRepository.Callback<Post> {
                override fun onSuccess(data: Post) {

                    newPostUseCaseContainer.removePostUseCase(post.id)
                    liveData.replace(post.id, data.id, PostModel(data))
                }

                override fun onFailure(e: Exception) {
                    liveData.insert(post.id, PostModel(post, statusError = true))
                }
            })
    }
}