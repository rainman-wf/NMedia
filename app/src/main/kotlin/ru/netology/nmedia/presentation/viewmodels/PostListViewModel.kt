package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.container.PostListUseCaseContainer

class PostListViewModel(
    val liveData: PostsLiveData,
    private val postListUseCaseContainer: PostListUseCaseContainer
) : ViewModel() {

    init {
        liveData.update(FeedModel(statusLoading = true))
        syncData()
    }

    fun onRefreshSwiped() {
        liveData.setUpdateStatus()
        syncData()
    }

    private fun syncData() {
        postListUseCaseContainer.syncDataUseCase(
            object : PostRepository.Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    log("success")
                    liveData.update(
                        FeedModel(
                            posts = postListUseCaseContainer.getAllUseCase()
                                .associateBy { it.post.id }
                                .toMutableMap()
                        )
                    )
                }

                override fun onFailure(e: Exception) {
                    log("failure")
                    liveData.update(
                        FeedModel(
                            posts = postListUseCaseContainer.getAllUseCase()
                                .associateBy { it.post.id }
                                .toMutableMap(),
                            statusError = true
                        )
                    )
                }
            }
        )
    }

    fun onTryClicked(id: Long) {

        val post = liveData.posts.value?.posts?.get(id)?.post ?: return

        liveData.insert(post.id, PostModel(post, statusLoading = true))

        postListUseCaseContainer.sendPostUseCase(
            post.content,
            object : PostRepository.Callback<Post> {
                override fun onSuccess(data: Post) {
                    postListUseCaseContainer.removePostUseCase(post.id)
                    liveData.replace(post.id, data.id, PostModel(data))
                }

                override fun onFailure(e: Exception) {
                    liveData.insert(post.id, PostModel(post, statusError = true))
                }
            })
    }

    fun onLikeClicked(id: Long) {
        liveData.updateItem(postListUseCaseContainer.likePostUseCase(id))
    }

    fun onRemoveClicked(id: Long) {
        postListUseCaseContainer.removePostUseCase(id)
        liveData.removeItem(id)
    }
}