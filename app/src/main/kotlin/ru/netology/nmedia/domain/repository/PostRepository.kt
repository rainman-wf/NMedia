package ru.netology.nmedia.domain.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

interface PostRepository {
    fun addNew(newPost: NewPost) : Post
    fun updateContent(updatePostContent: UpdatePostContent) : Boolean
    fun getAll(): LiveData<List<Post>>
    fun like(id: Long) : Boolean
    fun share(id: Long): Boolean
    fun remove(id: Long): Boolean
    fun getById(id: Long): Post
    fun getObservableById(id: Long): LiveData<Post>
    fun addIncoming(post: Post): Long
}