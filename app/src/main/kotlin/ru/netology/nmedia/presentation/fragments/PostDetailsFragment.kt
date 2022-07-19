package ru.netology.nmedia.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.di.AppContainerHolder
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostViewHolder
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
        val navController = findNavController()

        val postViewHolder = PostViewHolder(
            binding.postCard,
            object : OnPostClickListener {
                override fun onLike(post: Post) {
                    viewModel.onLikeClicked(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, "Chose")
                    startActivity(shareIntent)
                }

                override fun onEdit(post: Post) {
                    navController.navigate(
                        PostDetailsFragmentDirections.actionPostDetailsFragmentToNewPostFragment(
                            postId, binding.postCard.content.toString()
                        )
                    )
                }

                override fun onRemove(post: Post) {
                    navController.navigateUp()
                    viewModel.onRemoveClicked(postId)
                }

                override fun onDetails(post: Post) {}

                override fun onTryClicked(post: Post) {}

                override fun onCancelClicked(post: Post) {}

            }
        )

        viewModel.liveData.posts
            .observe(viewLifecycleOwner) { posts ->
                val postModel = posts.posts[postId] ?: run {
                    findNavController().navigateUp()
                    return@observe
                }

                postViewHolder.bind(postModel)
            }
    }
}


