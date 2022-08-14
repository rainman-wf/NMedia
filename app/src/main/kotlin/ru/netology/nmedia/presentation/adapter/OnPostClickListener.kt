package ru.netology.nmedia.presentation.adapter

import ru.netology.nmedia.domain.models.PostModel

interface OnPostClickListener {
    fun onLike(postModel: PostModel)
    fun onShare(postModel: PostModel)
    fun onEdit(postModel: PostModel)
    fun onRemove(postModel: PostModel)
    fun onDetails(postModel: PostModel)
    fun onTryClicked(postModel: PostModel)
    fun onCancelClicked(postModel: PostModel)
}