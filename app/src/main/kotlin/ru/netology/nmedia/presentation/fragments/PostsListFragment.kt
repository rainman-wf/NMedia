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
import ru.netology.nmedia.presentation.viewmodels.PostListViewModel
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.domain.models.PostModel

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
                override fun onLike(postModel: PostModel) {
                    viewModel.onLikeClicked(postModel.key)
                }

                override fun onShare(postModel: PostModel) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unsupported),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onEdit(postModel: PostModel) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment(
                            postId = postModel.key,
                            postContent = postModel.post.content
                        )
                    )
                }

                override fun onRemove(postModel: PostModel) {
                    viewModel.onRemoveClicked(postModel.key)
                }

                override fun onDetails(postModel: PostModel) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToPostDetailsFragment(
                            postId = postModel.key
                        )
                    )
                }

                override fun onTryClicked(postModel: PostModel) {
                    viewModel.onTryClicked(postModel.key)
                }

                override fun onCancelClicked(postModel: PostModel) {
                    viewModel.onRemoveClicked(postModel.key)
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

        viewModel.liveData.data.observe(viewLifecycleOwner) { feedModel ->

            val postList = mutableListOf<PostModel>()

            postList.addAll(feedModel.posts.values.filterNot { it.post.id == 0L }.sortedBy { it.post.id }.reversed())
            postList.addAll( feedModel.posts.values.filterNot { it.post.id != 0L }.sortedBy { it.post.id }.reversed())
            postAdapter.submitList(postList)
        }

        viewModel.liveData.state.observe(viewLifecycleOwner) {
            binding.postList.isVisible = !it.loading
            binding.loadingGroup.isVisible = it.loading
            binding.updateList.isRefreshing = it.refreshing
        }

        binding.updateList.setOnRefreshListener {
            viewModel.onRefreshSwiped()
        }
    }
}