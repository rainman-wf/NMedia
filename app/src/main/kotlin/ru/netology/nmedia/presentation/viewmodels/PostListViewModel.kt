package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.interactor.PostListInteractor
import ru.netology.nmedia.domain.usecase.params.NewPostParam

class PostListViewModel(
    val liveData: PostsLiveData,
    private val postListInteractor: PostListInteractor
) : ViewModel() {

    init {
        liveData.update(FeedModel(statusLoading = true))
        syncData()
    }

    fun syncData() {
        val unsent = postListInteractor.getAllWorkpiecesUseCase.invoke()
            .map { PostModel(it, statusError = true) }
        val mutable = mutableListOf<PostModel>()
        mutable.addAll(unsent)

        postListInteractor.getAllUseCase.invoke(
            object : PostRepository.Callback<List<Post>> {
                override fun onSuccess(data: List<Post>) {

                    mutable.addAll(data.map { PostModel(it) })
                    liveData.update(FeedModel(posts = mutable.associateBy { it.post.id }
                        .toMutableMap(), statusSuccess = true))
                }

                override fun onFailure(e: Exception) {
                    liveData.update(FeedModel(posts = mutable.associateBy { it.post.id }
                        .toMutableMap(), statusSuccess = true))
                }
            }
        )
    }

    fun trySend(id: Long) {

        val post = liveData.posts.value?.posts?.get(id)?.post ?: return

        val newPostParam = NewPostParam(post.author, post.content)

        liveData.insert(post.id, PostModel(post, statusLoading = true))

        postListInteractor.sendPostUseCase.invoke(
            newPostParam,
            object : PostRepository.Callback<Post> {
                override fun onSuccess(data: Post) {

                    postListInteractor.removeWorkpieceUseCase.invoke(post.id - 2000000000)
                    liveData.replace(post.id, data.id, PostModel(data))
                }

                override fun onFailure(e: Exception) {
                    liveData.insert(post.id, PostModel(post, statusError = true))
                }
            })
    }

    fun like(id: Long) {
        val post = postListInteractor.likePostUseCase.invoke(id)
        log(post.isLiked)
        liveData.updateItem(post)
    }

    fun remove(id: Long) {
        postListInteractor.removePostUseCase.invoke(id)
        liveData.removeItem(id)
    }
}