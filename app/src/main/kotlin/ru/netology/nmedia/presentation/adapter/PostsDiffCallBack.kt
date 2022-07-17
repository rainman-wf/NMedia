package ru.netology.nmedia.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.PostModel

class PostsDiffCallBack : DiffUtil.ItemCallback<PostModel>() {

    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
        oldItem.post.id == newItem.post.id && oldItem.post.dateTime == newItem.post.dateTime

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
        oldItem == newItem
}