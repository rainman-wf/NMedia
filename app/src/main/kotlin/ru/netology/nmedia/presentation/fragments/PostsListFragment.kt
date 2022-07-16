package ru.netology.nmedia.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostAdapter
import ru.netology.nmedia.di.AppContainerHolder
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.presentation.viewmodels.PostListViewModel
import ru.netology.nmedia.databinding.FragmentPostsListBinding

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private val viewModel: PostListViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as AppContainerHolder).appContainer.postListViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostsListBinding.bind(view)
        val navController = findNavController()

        val postAdapter = PostAdapter(
            object : OnPostClickListener {
                override fun onLike(post: Post) {
                    viewModel.onLikeClicked(post.id)
                }

                override fun onShare(post: Post) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unsupported),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onEdit(post: Post) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment(
                            postId = post.id,
                            postContent = post.content
                        )
                    )
                }

                override fun onRemove(post: Post) {
                    viewModel.onRemoveClicked(post.id)
                }

                override fun onDetails(post: Post) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToPostDetailsFragment(
                            postId = post.id
                        )
                    )
                }

                override fun onTryClicked(post: Post) {
                    viewModel.onTryClicked(post.id)
                }

                override fun onCancelClicked(post: Post) {
                    viewModel.onRemoveClicked(post.id)
                }
            }
        )

        binding.postList.apply {
            adapter = postAdapter
            val animator = itemAnimator
            if (animator is DefaultItemAnimator) animator.supportsChangeAnimations = false
            postAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.addPost.setOnClickListener {
            navController.navigate(
                PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment()
            )
        }

        viewModel.liveData.posts.observe(viewLifecycleOwner) { feedModel ->
            binding.loadingGroup.isVisible = feedModel.statusLoading
            binding.emptyWall.isVisible = feedModel.posts.isEmpty() && !feedModel.statusLoading
            binding.updateList.isRefreshing = feedModel.statusUpdating
            postAdapter.submitList(feedModel.posts.values.sortedBy { it.post.dateTime }.reversed())
        }

        binding.updateList.setOnRefreshListener {
            viewModel.onRefreshSwiped()
        }
    }
}