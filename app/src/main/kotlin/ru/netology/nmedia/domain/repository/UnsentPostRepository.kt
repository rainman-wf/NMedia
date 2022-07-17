package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.Post

interface UnsentPostRepository {
    fun save (id: Long, content: String) : Post
    fun remove (id: Long) : Int
    fun getAll() : List<Post>
    fun getById(id: Long) : Post
}