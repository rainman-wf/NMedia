package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.Post

interface PostRepository {

    fun send (content: String, callback: Callback<Post>)
    fun getAll (callback: Callback<List<Post>>)
    fun like (id: Long) : Post
    fun remove (id: Long) : Int
    fun update (id: Long, content: String) : Post

    interface Callback<T> {
        fun onSending() {}
        fun onSuccess(data: T)
        fun onFailure(e: Exception)
    }

}
