package ru.netology.nmedia.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.formatDate
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.presentation.activities.MainActivity
import ru.netology.nmedia.presentation.viewmodels.DetailsViewModel

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private val viewModel: DetailsViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as MainActivity).appContainer.detailsViewModelFactory
    }

    private val args by navArgs<PostDetailsFragmentArgs>()
    private val postId by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPostDetailsBinding.bind(view)

        viewModel.observable(postId)
            .observe(viewLifecycleOwner) { post ->
                binding.postCard.apply {
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

                    likeCount.setOnClickListener {
                        viewModel.onLikeClicked(post.id)
                    }
                    sharesCount.setOnClickListener {
                        viewModel.onShareClicked(post.id)
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, post.content)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(intent, "Chose")
                        startActivity(shareIntent)
                    }

                    menu.setOnClickListener { showPopupMenu(menu) }
                    playButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.firstUrl?.url))
                        startActivity(intent)
                    }
                }
            }
    }

    private fun showPopupMenu(view: View) {
        val navController = findNavController()
        with(PopupMenu(view.context, view)) {
            inflate(R.menu.post_option_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemEdit -> {
                        navController.navigate(
                            PostDetailsFragmentDirections.actionPostDetailsFragmentToNewPostFragment(
                                postId
                            )
                        )
                        true
                    }
                    R.id.menuItemRemove -> {
                        findNavController().navigateUp()
                        viewModel.onRemoveClicked(postId)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
}


