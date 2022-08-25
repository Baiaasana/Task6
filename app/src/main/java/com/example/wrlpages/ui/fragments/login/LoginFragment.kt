package com.example.wrlpages.ui.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.R
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentLoginBinding
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentListener()
    }

    private fun setFragmentListener() {
        setFragmentResultListener(Constants.REQUEST_KEY) { _, bundle ->
            val resultEmail = bundle.getString(Constants.BUNDLE_KEY, "Default value")
            val resultPassword = bundle.getString(Constants.BUNDLE_KEY_PAS, "Default value")
            binding.etPassword.setText(resultPassword)
            binding.etEmail.setText(resultEmail)
        }
    }

    override fun listeners() {
        binding.btnLogin.setOnClickListener {
            when {
                isEmptyField() -> {
                    Toast.makeText(context,
                        getString(R.string.empty_fields_error),
                        Toast.LENGTH_SHORT)
                        .show()
                }
                !isValidEmail() -> {
                    Toast.makeText(context,
                        getString(R.string.invalid_email_error),
                        Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    viewModel.login(email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString())
                }
            }
            if (binding.rememberMe.isChecked) {
                observer()
            } else {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(binding.etEmail.text.toString()))
            }
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect {
                    when (it) {
                        is Resource.Success -> {
                            if (binding.rememberMe.isChecked) {
                                viewModel.save(Constants.KEY_TOKEN, it.data?.token.toString())
                                viewModel.save(Constants.KEY_EMAIL, binding.etEmail.text.toString())
                                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(binding.etEmail.text.toString()))
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(context,
                                getString(R.string.email_password_incorrect) + "\n ${it.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loader -> {
                            binding.progressBar.isVisible = it.loading
                        }
                    }
                }
            }
        }
    }

    private fun isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()

    private fun isEmptyField(): Boolean = with(binding) {
        return@with binding.etEmail.text.toString().isEmpty() ||
                binding.etPassword.text.toString().isEmpty()
    }

    override fun init() {
    }

    override fun observers() {
    }
}