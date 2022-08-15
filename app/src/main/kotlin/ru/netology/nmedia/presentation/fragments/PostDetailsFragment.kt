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
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostViewHolder
import ru.netology.nmedia.presentation.viewmodels.DetailsViewModel

class PostDetailsFragment : Fragment(R.layout.fragment_post_details) {

    private val viewModel: DetailsViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as AppContainerHolder).appContainer.detailsViewModelFactory
    }

    private val args by navArgs<PostDetailsFragmentArgs>()
    private val postModeKey by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostDetailsBinding.bind(view)
        val navController = findNavController()

        val postViewHolder = PostViewHolder(
            binding.postCard,
            object : OnPostClickListener {
                override fun onLike(postModel: PostModel) {
                    viewModel.onLikeClicked(postModel.key)
                }

                override fun onShare(postModel: PostModel) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, postModel.post.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, "Chose")
                    startActivity(shareIntent)
                }

                override fun onEdit(postModel: PostModel) {
                    navController.navigate(
                        PostDetailsFragmentDirections.actionPostDetailsFragmentToNewPostFragment(
                            postModeKey, binding.postCard.content.toString()
                        )
                    )
                }

                override fun onRemove(postModel: PostModel) {
                    navController.navigateUp()
                    viewModel.onRemoveClicked(postModeKey)
                }

                override fun onDetails(postModel: PostModel) {}

                override fun onTryClicked(postModel: PostModel) {}

                override fun onCancelClicked(postModel: PostModel) {}

            }
        )

        viewModel.modelsLiveData.data
            .observe(viewLifecycleOwner) { posts ->
                val postModel = posts.posts[postModeKey] ?: run {
                    findNavController().navigateUp()
                    return@observe
                }

                postViewHolder.bind(postModel)
            }
    }
}


