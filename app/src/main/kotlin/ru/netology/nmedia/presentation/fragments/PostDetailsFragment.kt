package ru.netology.nmedia.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.common.utils.asUnit
import ru.netology.nmedia.common.utils.formatDate
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.di.AppContainerHolder
import ru.netology.nmedia.presentation.viewmodels.DetailsViewModel

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private val viewModel: DetailsViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as AppContainerHolder).appContainer.detailsViewModelFactory
    }

    private val args by navArgs<PostDetailsFragmentArgs>()
    private val postId by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPostDetailsBinding.bind(view)

        viewModel.liveData.posts
            .observe(viewLifecycleOwner) { posts ->

                val postModel = posts.posts[postId] ?: kotlin.run {
                    findNavController().navigateUp()
                    return@observe
                }

                val post = postModel.post
                binding.postCard.apply {
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

                    likeCount.setOnClickListener {
                        viewModel.onLikeClicked(post.id)
                    }
                    sharesCount.setOnClickListener {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, post.content)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(intent, "Chose")
                        startActivity(shareIntent)
                    }

                    menu.setOnClickListener { showPopupMenu(menu, post.content) }
                }
            }
    }

    private fun showPopupMenu(view: View, content: String) {
        val navController = findNavController()
        with(PopupMenu(view.context, view)) {
            inflate(R.menu.post_option_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemEdit -> {
                        navController.navigate(
                            PostDetailsFragmentDirections.actionPostDetailsFragmentToNewPostFragment(
                                postId, content
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


