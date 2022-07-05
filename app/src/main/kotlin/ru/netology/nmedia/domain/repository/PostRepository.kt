package ru.netology.nmedia.domain.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

interface PostRepository {
    fun addNew(newPost: NewPost): Post
    fun updateContent(updatePostContent: UpdatePostContent): Post
    fun getAll(): List<Post>
    fun like(id: Long): Post
    fun share(id: Long): Post
    fun remove(id: Long): Boolean
    fun getById(id: Long): Post
    fun getObservableById(id: Long): LiveData<Post>
    fun addIncoming(post: Post): Long
}
