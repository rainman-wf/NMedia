package ru.netology.nmedia.presentation.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
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

        val isSignUp = reason == "signUp"

        binding.singUpGroup.isVisible = isSignUp

        authViewModel.errorEvent.observe(viewLifecycleOwner) { message ->
            log(message)
            message?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        }

        authViewModel.okEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.edit_content_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.save -> {
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
                                    if (isSignUp) authViewModel.signUp(
                                        binding.loginInputText.text.toString(),
                                        binding.passwordInputText.text.toString(),
                                        binding.firstNameInputText.text.toString()
                                    )
                                    else authViewModel.signIn(
                                        binding.loginInputText.text.toString(),
                                        binding.passwordInputText.text.toString()
                                    )
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
            }
        )
    }
}