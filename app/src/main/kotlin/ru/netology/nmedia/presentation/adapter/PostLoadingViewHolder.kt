package ru.netology.nmedia.presentation.adapter

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.databinding.ItemLoadingBinding

class PostLoadingViewHolder(
    private val itemLoadingBinding: ItemLoadingBinding,
    private val retryListener: () -> Unit
) : RecyclerView.ViewHolder(
    itemLoadingBinding.root
) {

    fun bind(loadState: LoadState) {

        log("STATE : ${loadState::class.simpleName}")

        itemLoadingBinding.apply {
            progress.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error && loadState.error !is NoSuchElementException
            retryButton.setOnClickListener {
                retryListener()
            }
        }
    }
}