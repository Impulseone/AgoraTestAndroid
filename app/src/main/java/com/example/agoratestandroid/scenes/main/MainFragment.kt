package com.example.agoratestandroid.scenes.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.SceneMainBinding
import com.example.agoratestandroid.scenes.login.LoginFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.scene_main) {

    private val binding: SceneMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindLogoutButton()
    }

    private fun bindLogoutButton() {
        binding.logoutBt.setOnClickListener{
            viewModel.logout()
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLogoutSuccessFlow.collectLatest {
                    if (it) findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                    else Log.e(TAG, "logout failed")
                }
            }
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }
}