package ru.netology.nmedia.presentation.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.Post

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onPostClickListener: OnPostClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.apply {
            author.text = post.author.name
            content.text = post.content
            published.text = post.published.toString()
            likeCount.text = post.likes.asUnit()
            sharesCount.text = post.shares.asUnit()
            viewsCount.text = post.views.asUnit()
            likeCount.isChecked = post.likedByMe

            menu.isVisible = post.ownedByMe

            post.attachment?.let {
                Glide.with(attachmentImage)
                    .load("http://10.0.2.2:9999/media/${it.url}")
                    .timeout(10_000)
                    .into(attachmentImage)

            } ?: run {
                attachmentImage.isVisible = false
            }

            if (!post.author.avatar.isNullOrBlank())
                Glide.with(avatar)
                    .load("http://10.0.2.2:9999/avatars/${post.author.avatar}")
                    .timeout(10_000)
                    .placeholder(R.mipmap.ic_avatar)
                    .circleCrop()
                    .into(avatar)


            likeCount.setOnClickListener { onPostClickListener.onLike(post) }
            sharesCount.setOnClickListener { onPostClickListener.onShare(post) }
            menu.setOnClickListener { showPopupMenu(menu, post) }
            itemContainer.setOnClickListener { onPostClickListener.onDetails(post) }
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