package ru.netology.nmedia.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.domain.models.FeedModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel

class PostsLiveData {

    private val _posts = MutableLiveData(FeedModel(mutableMapOf()))
    val posts: LiveData<FeedModel>
        get() = _posts

    fun update(feedModel: FeedModel) {
        _posts.postValue(feedModel)
    }

    fun insert(id: Long, postModel: PostModel) {
        val map = _posts.value?.posts
        map?.put(id, postModel)
        _posts.postValue(FeedModel(map?: mutableMapOf()))
    }

    fun replace(oldId: Long, newId: Long, postModel: PostModel) {
        val map = _posts.value?.posts
        map?.remove(oldId)
        map?.set(newId, postModel)
        _posts.postValue(FeedModel(map?: mutableMapOf()))
    }

    fun updateItem(post: Post) {
        val map = _posts.value?.posts
        map?.put(post.id, PostModel(post))
        _posts.postValue(FeedModel(map?: mutableMapOf()))
    }

    fun removeItem(id: Long) {
        val map = _posts.value?.posts
        map?.remove(id)
        _posts.postValue(FeedModel(map?: mutableMapOf()))
    }
}