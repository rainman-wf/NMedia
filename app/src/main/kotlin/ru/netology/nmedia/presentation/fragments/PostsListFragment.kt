package ru.netology.nmedia.presentation.fragments

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
import ru.netology.nmedia.common.utils.log
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
                    viewModel.like(post.id)
                }

                override fun onShare(post: Post) {
                    TODO ("unsupported function")
                }

                override fun onEdit(post: Post) {

                }

                override fun onRemove(post: Post) {
                    viewModel.remove(post.id)
                }

                override fun onDetails(post: Post) {

                }

                override fun onTryClicked(post: Post) {
                    viewModel.trySend(post.id)
                }

                override fun onCancelClicked(post: Post) {

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
            binding.emptyWall.isVisible = feedModel.statusEmpty && !feedModel.statusLoading
            binding.updateList.isRefreshing = feedModel.statusUpdating
            postAdapter.submitList(feedModel.posts.values.sortedBy { it.post.dateTime }.reversed())
        }


        binding.updateList.setOnRefreshListener {
            viewModel.syncData()
        }



//        viewModel.data.observe(viewLifecycleOwner) {
//            binding.loadingGroup.isVisible = it.statusLoading
//            binding.updateList.isRefreshing = it.statusUpdating
//            binding.emptyWall.isVisible = it.statusEmpty
//            binding.errorMessage.isVisible = it.statusError
//            binding.errorMessage.text = it.errorMsg
//
//        }
    }
}