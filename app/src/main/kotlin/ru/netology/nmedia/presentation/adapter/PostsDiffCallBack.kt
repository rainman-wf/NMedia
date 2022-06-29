package ru.netology.nmedia.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.domain.models.Post

class PostsDiffCallBack : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}