package ru.netology.nmedia.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.TimeSeparatorBinding
import ru.netology.nmedia.domain.models.TimeSeparator

class TimeSeparatorViewHolder(
    private val binding: TimeSeparatorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(timeSeparator: TimeSeparator) {
        binding.timeSeparator.text = timeSeparator.time
    }
}