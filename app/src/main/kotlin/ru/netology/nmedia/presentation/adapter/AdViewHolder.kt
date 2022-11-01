package ru.netology.nmedia.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.domain.models.Ad

class AdViewHolder(
    private val binding: CardAdBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {

        Glide.with(binding.adImage)
            .load("http://10.0.2.2:9999/media/${ad.image}")
            .timeout(10_000)
            .into(binding.adImage)
    }
}

