package ru.netology.nmedia.presentation.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.common.constants.AuthFragmentArgsConstants
import ru.netology.nmedia.common.exceptions.DbError
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostAdapter
import ru.netology.nmedia.presentation.viewmodels.PostListViewModel
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.domain.models.PostModel

@AndroidEntryPoint
class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private val viewModel: PostListViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostsListBinding.bind(view)
        val navController = findNavController()

        viewModel.modelsLiveData.authData.observe(viewLifecycleOwner) {
            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.manu_main, menu)
                    menu.let {
                        it.setGroupVisible(
                            R.id.unauthenticated,
                            !viewModel.modelsLiveData.authenticated
                        )
                        it.setGroupVisible(
                            R.id.authenticated,
                            viewModel.modelsLiveData.authenticated
                        )
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.signIn -> {
                            navController.navigate(
                                PostsListFragmentDirections.actionPostsListFragmentToSignUpFragment(
                                    AuthFragmentArgsConstants.SIGN_IN.name
                                )
                            )
                            true
                        }
                        R.id.signOut -> {
                            AppAuth.getInstance().removeAuth()
                            true
                        }
                        R.id.signUp -> {
                            navController.navigate(
                                PostsListFragmentDirections.actionPostsListFragmentToSignUpFragment(
                                    AuthFragmentArgsConstants.SIGN_UP.name
                                )
                            )
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner)
        }

        val postAdapter = PostAdapter(
            object : OnPostClickListener {
                override fun onLike(postModel: PostModel) {
                    if (viewModel.modelsLiveData.authenticated) viewModel.onLikeClicked(postModel.key)
                    else showAuthAlert(navController)
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
            if (viewModel.modelsLiveData.authenticated)
                navController.navigate(
                    PostsListFragmentDirections.actionPostsListFragmentToNewPostFragment()
                )
            else showAuthAlert(navController)
        }

        viewModel.modelsLiveData.data.observe(viewLifecycleOwner) { feedModel ->
            val unread = feedModel.posts.values.filterNot { it.read }
            binding.newerCount.text = "${getText(R.string.new_posts)} ${unread.size}"
            binding.newerCount.isVisible = unread.isNotEmpty()
            postAdapter.submitList(feedModel.posts.values.sortedBy { it.post.published }.reversed())
        }

        binding.newerCount.setOnClickListener {
            binding.postList.layoutManager?.scrollToPosition(0)
        }

        binding.postList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager
                val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPosition = layoutManager.itemCount - 1
                for (position in currentPosition..lastPosition) {
                    try {
                        val post = postAdapter.getPost(position)
                        if (!post.read) viewModel.setRead(post.key)
                    } catch (_: Exception) {
                    }
                }
            }
        })

        viewModel.modelsLiveData.postCreated.observe(viewLifecycleOwner) {
            try {
                viewModel.onTryClicked(it!!)
            } catch (e: Exception) {
                throw DbError
            }
        }

        viewModel.modelsLiveData.state.observe(viewLifecycleOwner) {
            binding.postList.isVisible = !it.loading
            binding.loadingText.isVisible = it.loading
            binding.loadingProgress.isVisible = it.loading
            binding.updateList.isRefreshing = it.refreshing
        }

        binding.updateList.setOnRefreshListener {
            viewModel.onRefreshSwiped()
        }
    }

    fun showAuthAlert(navController: NavController) {
        AlertDialog.Builder(requireActivity())
            .setTitle("You are not authorized")
            .setMessage("Select \"Sign in\" if you have an account or \"Sign up\" to register a new one")
            .setNegativeButton("Sign in") { _, _ ->
                navController.navigate(
                    PostsListFragmentDirections.actionPostsListFragmentToSignUpFragment(
                        AuthFragmentArgsConstants.SIGN_IN.name
                    )
                )
            }
            .setPositiveButton("Sign up") { _, _ ->
                navController.navigate(
                    PostsListFragmentDirections.actionPostsListFragmentToSignUpFragment(
                        AuthFragmentArgsConstants.SIGN_UP.name
                    )
                )
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }
}