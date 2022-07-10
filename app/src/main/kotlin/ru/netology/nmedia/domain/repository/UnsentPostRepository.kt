package ru.netology.nmedia.domain.repository

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.usecase.params.NewPostParam

interface UnsentPostRepository {
    fun save (newPostParam: NewPostParam) : Post
    fun remove (id: Long) : Int
    fun getAllUnsent() : List<Post>
}