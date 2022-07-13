package ru.netology.nmedia.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.common.utils.notifyEmptyMessage
import ru.netology.nmedia.di.AppContainerHolder
import ru.netology.nmedia.presentation.viewmodels.NewPostViewModel

class NewPostFragment : Fragment(R.layout.fragment_new_post) {

    private val viewModel: NewPostViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as AppContainerHolder).appContainer.newPostViewModelFactory
    }

    private val args: NewPostFragmentArgs by navArgs()
    private val postId by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewPostBinding.bind(view)
        val navController = findNavController()

        if (postId != 0L) {
            binding.msgInputText.setText(args.postContent)
        }

        binding.apply {
            save.setOnClickListener {
                if (msgInputText.text.isEmpty()) {
                    notifyEmptyMessage(binding.root)
                    return@setOnClickListener
                }

                viewModel.onSaveClicked(args.postId, msgInputText.text.toString())

                navController.navigateUp()
                msgInputText.text.clear()

            }

            cancel.setOnClickListener {
                navController.navigateUp()
                msgInputText.text.clear()
            }

            msgInputText.requestFocus()
        }
    }
}