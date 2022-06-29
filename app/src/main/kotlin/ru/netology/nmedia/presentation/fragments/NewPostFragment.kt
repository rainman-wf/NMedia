package ru.netology.nmedia.presentation.fragments

import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.common.utils.notifyEmptyMessage
import ru.netology.nmedia.presentation.activities.MainActivity
import ru.netology.nmedia.presentation.viewmodels.NewPostViewModel

class NewPostFragment : Fragment(R.layout.fragment_new_post) {

    private val viewModel: NewPostViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as MainActivity).appContainer.newPostViewModelFactory
    }

    private val args: NewPostFragmentArgs by navArgs()
    private val postId by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewPostBinding.bind(view)
        val navController = findNavController()

        if (args.postId != 0L) viewModel.getPost(postId)
            .apply { binding.msgInputText.setText(content) }

        binding.apply {
            save.setOnClickListener {
                if (msgInputText.text.isEmpty()) {
                    notifyEmptyMessage(binding.root)
                    return@setOnClickListener
                }
                if (postId != 0L) viewModel.onSaveClicked(
                    postId,
                    msgInputText.text.toString(),
                    getUrl(msgInputText)
                )
                else viewModel.onSaveClicked(
                    "Dinar",
                    msgInputText.text.toString(),
                    getUrl(msgInputText)
                )

                navController.navigateUp()
                msgInputText.text.clear()
            }

            cancel.setOnClickListener {
                navController.navigateUp()
                msgInputText.text.clear()
            }
        }
    }

    private fun getUrl(editText: EditText): String? {
        Linkify.addLinks(editText, Linkify.WEB_URLS)
        return if (editText.urls.isNotEmpty()) editText.urls[0].url else null
    }
}