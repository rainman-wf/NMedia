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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.common.constants.AuthFragmentArgsConstants
import ru.netology.nmedia.data.auth.AppAuth
import ru.netology.nmedia.presentation.adapter.OnPostClickListener
import ru.netology.nmedia.presentation.adapter.PostAdapter
import ru.netology.nmedia.presentation.viewmodels.PostListViewModel
import ru.netology.nmedia.databinding.FragmentPostsListBinding
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.presentation.adapter.PostLoadingStateAdapter

@AndroidEntryPoint
class PostsListFragment : Fragment(R.layout.fragment_posts_list) {

    private val viewModel: PostListViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostsListBinding.bind(view)
        val navController = findNavController()

        val postAdapter = PostAdapter(
            object : OnPostClickListener {
                override fun onLike(post: Post) {
                    if (viewModel.modelsLiveData.authenticated) viewModel.onLikeClicked(post.id)
                    else showAuthAlert(navController)
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

            }
        )

        viewModel.modelsLiveData.authData.observe(viewLifecycleOwner) {

            postAdapter.refresh()

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

        binding.postList.apply {
            adapter = postAdapter.withLoadStateHeaderAndFooter(
                header = PostLoadingStateAdapter { postAdapter.retry() },
                footer = PostLoadingStateAdapter { postAdapter.retry() },
            )
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

        lifecycleScope.launchWhenCreated {
            viewModel.modelsLiveData.data
                .collectLatest {
                    postAdapter.submitData(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            postAdapter.loadStateFlow.collectLatest {
                binding.updateList.isRefreshing =
                    it.refresh is LoadState.Loading ||
                            it.append is LoadState.Loading ||
                            it.prepend is LoadState.Loading
            }
        }

        binding.updateList.setOnRefreshListener {
            postAdapter.refresh()
        }

        viewModel.modelsLiveData.postCreated.observe(viewLifecycleOwner) {
            it?.let { viewModel.sendPost(it.id, it.content) }
        }

        viewModel.modelsLiveData.postSent.observe(viewLifecycleOwner) {
            postAdapter.refresh()
            binding.postList.layoutManager?.scrollToPosition(0)
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