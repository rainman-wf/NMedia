package ru.netology.nmedia.presentation.adapter

import ru.netology.nmedia.domain.models.Post

interface OnPostClickListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onDetails(post: Post)
}