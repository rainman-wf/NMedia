package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.usecase.*
import kotlin.concurrent.thread

class PostListViewModel(
    private val likePostUseCase: LikePostUseCase,
    private val sharePostUseCase: SharePostUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val getPostsListUseCase: GetPostsListUseCase,
) : ViewModel() {

    private val _posts = MutableLiveData<FeedModel>()
    val posts: LiveData<FeedModel>
        get() = _posts

    init {
        _posts.postValue(FeedModel(statusLoading = true))
        loadPosts()
    }

    fun loadPosts() {
        thread {
            try {
                val posts = getPostsListUseCase.invoke()
                _posts.postValue(
                    FeedModel(
                        posts,
                        statusEmpty = posts.isEmpty(),
                        statusSuccess = posts.isNotEmpty(),
                        statusLoading = false
                    )
                )
            } catch (e: Exception) {
                _posts.postValue(
                    FeedModel(
                        statusError = true,
                        errorMsg = e::class.java.simpleName
                    )
                )
            }
        }
    }

    fun onLikeClicked(id: Long) {
        thread {
            val oldPosts = _posts.value?.posts ?: return@thread
            val targetPost = oldPosts.find { it.id == id } ?: return@thread

            // данную логику стот выполнить в LocalDataRepository, и сохранять пост в локальную
            // базу данных.

            val newPost = targetPost.let {
                it.copy(
                    isLiked = !it.isLiked,
                    likes = if (it.isLiked) it.likes - 1 else it.likes + 1
                )
            }

            val newPosts = oldPosts.filter { it.id != id }.toMutableList()
            newPosts.add(newPost)
            _posts.postValue(FeedModel(posts = newPosts))

            try {
                likePostUseCase.invoke(id)
            } catch (e: Exception) {
                // если пост был удален, то удалить его локально
                // если нет соденинения с сервером, повторить попытку позже.
            }
        }
    }

    fun onRemoveClicked(id: Long) {
        thread {
            val old = _posts.value?.posts.orEmpty()
            _posts.postValue(
                _posts.value?.copy(posts = _posts.value?.posts.orEmpty()
                    .filter { it.id != id })
            )

            try {
                removePostUseCase.invoke(id)
            } catch (e: Exception) {
                _posts.postValue(_posts.value?.copy(posts = old))
            }
        }

    }

    fun onShareClicked(id: Long) {
        sharePostUseCase.invoke(id)
    }

}