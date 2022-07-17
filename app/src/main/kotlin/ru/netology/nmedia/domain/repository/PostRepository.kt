package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.Post

interface PostRepository {

    fun send (content: String, callback: Callback<Post>)
    fun getAll () : List<Post>
    fun like (id: Long) : Post
    fun remove (id: Long) : Int
    fun update (id: Long, content: String) : Post
    fun getById(id: Long) : Post
    fun syncData (callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(data: T)
        fun onFailure(e: Exception)
    }
}
