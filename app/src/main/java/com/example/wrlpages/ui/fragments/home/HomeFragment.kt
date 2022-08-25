package com.example.wrlpages.ui.fragments.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wrlpages.Constants
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private val args: HomeFragmentArgs by navArgs()

    override fun listeners() {
        binding.btnLogout.setOnClickListener {
            logout()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
        }
        observer()
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPreferences().collect {
//                    if (it.contains(stringPreferencesKey(Constants.KEY))){
//                        binding.text.text = it[stringPreferencesKey(Constants.KEY)]
//                    }
//                    binding.text.text = it[stringPreferencesKey(Constants.KEY)]
//                        ?: "No token \n session does not \n exist"

                    binding.text.text = args.email
                }
            }
        }
    }

    override fun observers() {
    }

    override fun init() {
    }

    private fun logout() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.remove(Constants.KEY_TOKEN)
                viewModel.remove(Constants.KEY_EMAIL)
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
            }
        }
    }
}