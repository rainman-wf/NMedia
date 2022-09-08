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
import ru.netology.nmedia.R
import ru.netology.nmedia.common.constants.AuthFragmentArgsConstants
import ru.netology.nmedia.common.utils.hideKeyboard
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.di.AppContainerHolder
import ru.netology.nmedia.presentation.viewmodels.AuthViewModel

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val args: AuthFragmentArgs by navArgs()
    private val reason by lazy { args.reason }

    val authViewModel: AuthViewModel by viewModels(::requireParentFragment) {
        (requireActivity() as AppContainerHolder).appContainer.authViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAuthBinding.bind(view)

        val isSignUp =
            AuthFragmentArgsConstants.valueOf(reason) == AuthFragmentArgsConstants.SIGN_UP

        binding.singUpGroup.isVisible = isSignUp

        authViewModel.errorEvent.observe(viewLifecycleOwner) { message ->
            message?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        }

        authViewModel.okEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

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
                        it.data?.data?.let { uri ->  authViewModel.changePhoto(uri) }
                    }
                }
            }

        binding.pickAvatarPhoto.setOnClickListener {
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

        binding.takeAvatarPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        authViewModel.liveData.photo.observe(viewLifecycleOwner) { photoModel ->
            log(photoModel == null)
            photoModel?.uri?.let {
                Glide.with(binding.avatarPreview)
                    .load(it)
                    .placeholder(R.mipmap.ic_avatar)
                    .into(binding.avatarPreview)
            }
        }

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.edit_content_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.register -> {
                            log(menuItem.title)
                            binding.root.hideKeyboard()
                            when {
                                isSignUp && binding.firstNameInputText.text?.isEmpty() == true ->
                                    Snackbar.make(
                                        binding.firstNameInputText,
                                        "First name must be filled in",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                binding.loginInputText.text?.isEmpty() == true ->
                                    Snackbar.make(
                                        binding.loginInputText,
                                        "Login is Empty",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                binding.passwordInputText.text?.isEmpty() == true ->
                                    Snackbar.make(
                                        binding.passwordInputText,
                                        "Password is Empty",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                else -> {
                                    if (isSignUp) {
                                        authViewModel.signUp(
                                            binding.loginInputText.text.toString(),
                                            binding.passwordInputText.text.toString(),
                                            binding.firstNameInputText.text.toString()
                                        )
                                    } else {
                                        authViewModel.signIn(
                                            binding.loginInputText.text.toString(),
                                            binding.passwordInputText.text.toString()
                                        )
                                    }
                                }
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