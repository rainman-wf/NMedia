package ru.netology.nmedia.presentation.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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

//        requireActivity().actionBar
//
//        requireActivity().title = "Edit Post"
//        requireActivity().actionBar?.setHomeButtonEnabled(true)
//        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
//
//        requireActivity().addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.edit_content_menu, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return true
//            }
//
//        })

        val binding = FragmentNewPostBinding.bind(view)



        binding.msgInputText.setText(args.postContent)


        binding.apply {
            save.setOnClickListener {
                if (msgInputText.text.isEmpty()) {
                    notifyEmptyMessage(binding.root)
                    return@setOnClickListener
                }

                viewModel.onSaveClicked(postId, msgInputText.text.toString())


                msgInputText.text.clear()

            }

            cancel.setOnClickListener {

                msgInputText.text.clear()
            }

            msgInputText.requestFocus()
        }
    }
}