package ru.netology.nmedia.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.loadPosts()
        return super.onCreateView(inflater, container, savedInstanceState)
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
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(intent, "Chose")
                    startActivity(shareIntent)
                    viewModel.onShareClicked(post.id)
                }

                override fun onEdit(post: Post) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment(
                            post.id,
                            post.content
                        )
                    )
                }

                override fun onRemove(post: Post) {
                    viewModel.onRemoveClicked(post.id)
                }

                override fun onPlay(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.firstUrl?.url))
                    startActivity(intent)
                }

                override fun onDetails(post: Post) {
                    navController.navigate(
                        PostsListFragmentDirections.actionPostsListFragmentToPostDetailsFragment(
                            post.id
                        )
                    )
                }
            }
        )

        binding.updateList.setOnRefreshListener {
            viewModel.loadPosts()
            binding.updateList.isRefreshing = false
        }

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

        viewModel.posts.observe(viewLifecycleOwner) {
            binding.loadingGroup.isVisible = it.statusLoading
            if (it.statusSuccess) {
                postAdapter.submitList(it.posts.toList())
            }
            binding.emptyWall.isVisible = it.statusEmpty
            binding.errorMessage.isVisible = it.statusError
            binding.errorMessage.text = it.errorMsg
        }
    }
}