package com.example.wrlpages.ui.fragments.welcome

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.launch

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(
    FragmentWelcomeBinding::inflate) {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun listeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment())
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
            }
        }
    }

    private fun hasSession() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPreferences().collect {
                    if (it.contains(stringPreferencesKey(Constants.KEY_TOKEN))) {
                        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment(it[stringPreferencesKey(Constants.KEY_EMAIL)]))
                    }
                }
            }
        }
    }

    override fun init() {

        hasSession()
    }

    override fun observers() {
    }
}