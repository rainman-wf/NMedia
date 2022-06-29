package ru.netology.nmedia.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.presentation.activities.MainActivity
import ru.netology.nmedia.presentation.viewmodels.PostListViewModel

class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private val viewModel: PostListViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as MainActivity).appContainer.postListViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            log(it)
        }

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
                        PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment(post.id)
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

        binding.postList.apply {
            adapter = postAdapter
            val animator = itemAnimator
            if (animator is DefaultItemAnimator) animator.supportsChangeAnimations = false
        }

        binding.addPost.setOnClickListener {
            navController.navigate(
                PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment()
            )
        }

        viewModel.getAll().observe(viewLifecycleOwner) {
            postAdapter.submitList(it.toList().reversed())
        }
    }
}