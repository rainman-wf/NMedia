package ru.netology.nmedia.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.domain.models.FeedItem

class PostsDiffCallBack : DiffUtil.ItemCallback<FeedItem>() {

    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) return false
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean =
        oldItem == newItem
}