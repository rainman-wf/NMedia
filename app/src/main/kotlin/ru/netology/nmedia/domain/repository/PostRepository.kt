package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.UpdatePostDto

interface PostRepository {

    fun send (newPostDto: NewPostDto, callback: Callback<Post>)
    fun getAll () : List<Post>
    fun like (id: Long) : Post
    fun remove (id: Long)
    fun update (updatePostDto: UpdatePostDto) : Post
    fun getById(id: Long) : Post
    fun syncData (callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(data: T)
        fun onFailure(e: Exception)
    }
}
