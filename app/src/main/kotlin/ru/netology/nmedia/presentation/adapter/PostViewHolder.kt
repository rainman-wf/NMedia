package ru.netology.nmedia.presentation.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.constants.BASE_URL
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.formatDate
import ru.netology.nmedia.domain.models.PostModel

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onPostClickListener: OnPostClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(postModel: PostModel) {
        val post = postModel.post
        binding.apply {
            author.text = post.author
            content.text = post.content
            published.text = formatDate(post.dateTime)
            likeCount.text = post.likes.asUnit()
            sharesCount.text = post.shares.asUnit()
            viewsCount.text = post.views.asUnit()
            likeCount.isChecked = post.isLiked

            counters.isVisible = !postModel.statusError && !postModel.statusLoading
            error.isVisible = postModel.statusError
            sending.isVisible = postModel.statusLoading
            trySending.isEnabled = postModel.statusError
            cancel.isEnabled = postModel.statusError
            sendingBar.isVisible = postModel.statusError || postModel.statusLoading

            menu.isEnabled = post.author == AUTHOR

            if (!post.authorAvatar.isNullOrBlank())
                Glide.with(avatar)
                    .load("$BASE_URL/avatars/${postModel.post.authorAvatar}")
                    .placeholder(R.drawable.netology_logo)
                    .timeout(10_000)
                    .circleCrop()
                    .into(avatar)


            trySending.setOnClickListener { onPostClickListener.onTryClicked(post) }
            cancel.setOnClickListener { onPostClickListener.onCancelClicked(post) }
            likeCount.setOnClickListener { onPostClickListener.onLike(post) }
            sharesCount.setOnClickListener { onPostClickListener.onShare(post) }
            menu.setOnClickListener { showPopupMenu(menu, post) }
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