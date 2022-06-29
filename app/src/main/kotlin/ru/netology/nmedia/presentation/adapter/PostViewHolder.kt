package ru.netology.nmedia.presentation.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.formatDate
import ru.netology.nmedia.common.utils.log

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onPostClickListener: OnPostClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            content.text = post.content
            published.text = formatDate(post.dateTime)
            likeCount.text = post.likes.asUnit()
            sharesCount.text = post.shares.asUnit()
            viewsCount.text = post.views.asUnit()
            likeCount.isChecked = post.isLiked

            post.firstUrl?.thumbData?.apply {
                Glide.with(root.context)
                    .load(thumbnail_url)
                    .centerCrop()
                    .override(thumbnail_width, thumbnail_height)
                    .into(richLink)
                playButton.visibility = View.VISIBLE
            }

            likeCount.setOnClickListener { onPostClickListener.onLike(post) }
            sharesCount.setOnClickListener { onPostClickListener.onShare(post) }
            menu.setOnClickListener { showPopupMenu(menu, post) }
            playButton.setOnClickListener { onPostClickListener.onPlay(post) }
            itemConteiner.setOnClickListener { onPostClickListener.onDetails(post) }
        }
    }

    private fun showPopupMenu(view: View, post: Post) {
        with(PopupMenu(view.context, view)) {
            inflate(R.menu.post_option_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemEdit -> setListener(onPostClickListener.onEdit(post))
                    R.id.menuItemRemove -> setListener(onPostClickListener.onRemove(post))
                    else -> false
                }
            }
            show()
        }
    }

    private val setListener: (Unit) -> Boolean = { true }
}