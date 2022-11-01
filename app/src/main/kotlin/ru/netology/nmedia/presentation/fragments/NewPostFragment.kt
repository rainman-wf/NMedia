package ru.netology.nmedia.presentation.fragments

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.common.utils.notifyEmptyMessage
import ru.netology.nmedia.presentation.viewmodels.NewPostViewModel

@AndroidEntryPoint
class NewPostFragment : Fragment(R.layout.fragment_new_post) {

    private val viewModel: NewPostViewModel by viewModels()

    private val args: NewPostFragmentArgs by navArgs()
    private val postId by lazy { args.postId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewPostBinding.bind(view)
        binding.msgInputText.setText(args.postContent)

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        it.data?.data?.let { uri -> viewModel.changePhoto(uri) }
                    }
                }
            }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg"
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        viewModel.liveData.photo.observe(viewLifecycleOwner) { photoModel ->
            photoModel?.uri?.let {
                binding.clearAttachment.isVisible = true
                binding.attachmentImagePreview.isVisible = true
                Glide.with(binding.attachmentImagePreview)
                    .load(it)
                    .timeout(10_000)
                    .into(binding.attachmentImagePreview)
            } ?: run {
                binding.clearAttachment.isVisible = false
                binding.attachmentImagePreview.isVisible = false
            }
        }

        binding.clearAttachment.setOnClickListener { viewModel.clearAttachment() }

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.edit_content_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.register -> {
                            if (binding.msgInputText.text.isEmpty()) {
                                notifyEmptyMessage(binding.root)
                            } else {
                                viewModel.onSaveClicked(
                                    postId,
                                    binding.msgInputText.text.toString()
                                )
                                binding.msgInputText.text.clear()
                                findNavController().popBackStack()
                            }
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner
        )
    }
}