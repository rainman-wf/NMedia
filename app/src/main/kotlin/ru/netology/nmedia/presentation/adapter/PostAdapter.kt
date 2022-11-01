package ru.netology.nmedia.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.ItemPlaceHolderBinding
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.databinding.TimeSeparatorBinding
import ru.netology.nmedia.domain.models.Ad
import ru.netology.nmedia.domain.models.FeedItem
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.TimeSeparator

class PostAdapter(private val onPostClickListener: OnPostClickListener) :
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostsDiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Ad -> R.layout.card_ad
            is Post -> R.layout.post_card
            is TimeSeparator -> R.layout.time_separator
            null -> R.layout.item_place_holder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.post_card -> {
                val binding =
                    PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onPostClickListener)
            }
            R.layout.card_ad -> {
                val binding =
                    CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }
            R.layout.item_place_holder -> {
                val binding =
                    ItemPlaceHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StubViewHolder(binding)
            }
            R.layout.time_separator -> {
                val binding =
                    TimeSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TimeSeparatorViewHolder(binding)
            }
            else -> error("unknown item type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            is TimeSeparator -> (holder as? TimeSeparatorViewHolder)?.bind(item)
            null -> (holder as? StubViewHolder)?.bind()

        }
    }
}

