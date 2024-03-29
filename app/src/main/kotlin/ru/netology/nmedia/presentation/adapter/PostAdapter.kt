package ru.netology.nmedia.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.domain.models.PostModel

class PostAdapter(private val onPostClickListener: OnPostClickListener) :
    PagingDataAdapter<PostModel, PostViewHolder>(PostsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostCardBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding, onPostClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }

    fun getPost(position: Int): PostModel {
        return getItem(position) ?: error("post not found")
    }
}

