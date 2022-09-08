package ru.netology.nmedia.presentation.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.formatDate
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.domain.models.PostModel

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onPostClickListener: OnPostClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(postModel: PostModel) {
        val post = postModel.post

        binding.apply {
            author.text = post.author.name
            content.text = post.content
            published.text = formatDate(post.published)
            likeCount.text = post.likes.asUnit()
            sharesCount.text = post.shares.asUnit()
            viewsCount.text = post.views.asUnit()
            likeCount.isChecked = post.likedByMe

            counters.isVisible = postModel.state == PostModel.State.OK
            error.isVisible = postModel.state == PostModel.State.ERROR
            sending.isVisible = postModel.state == PostModel.State.LOADING
            trySending.isEnabled = postModel.state == PostModel.State.ERROR
            cancel.isEnabled =
                postModel.state == PostModel.State.LOADING || postModel.state == PostModel.State.ERROR
            sendingBar.isVisible =
                postModel.state == PostModel.State.LOADING || postModel.state == PostModel.State.ERROR

            menu.isVisible = post.author.id == AppAuth.getInstance().authStateFlow.value.id

            post.attachment?.let {
                log("attachment not null")
                Glide.with(attachmentImage)
                    .load(if (!it.url.contains("file://")) "http://10.0.2.2:9999/media/${it.url}" else it.url)
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


            trySending.setOnClickListener { onPostClickListener.onTryClicked(postModel) }
            cancel.setOnClickListener { onPostClickListener.onCancelClicked(postModel) }
            likeCount.setOnClickListener { onPostClickListener.onLike(postModel) }
            sharesCount.setOnClickListener { onPostClickListener.onShare(postModel) }
            menu.setOnClickListener { showPopupMenu(menu, postModel) }
            itemConteiner.setOnClickListener { onPostClickListener.onDetails(postModel) }
        }
    }

    private fun showPopupMenu(view: View, postModel: PostModel) {
        with(PopupMenu(view.context, view)) {
            inflate(R.menu.post_option_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemEdit -> setListener(onPostClickListener.onEdit(postModel))
                    R.id.menuItemRemove -> setListener(onPostClickListener.onRemove(postModel))
                    else -> false
                }
            }
            show()
        }
    }

    private val setListener: (Unit) -> Boolean = { true }
}