package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.usecase.params.NewPostParam

interface PostRepository {

    fun send (newPostParam: NewPostParam, callback: Callback<Post>)
    fun getAll (callback: Callback<List<Post>>)
    fun like (id: Long) : Post
    fun remove (id: Long)

    interface Callback<T> {
        fun onSending() {}
        fun onSuccess(data: T)
        fun onFailure(e: Exception)
    }

}
