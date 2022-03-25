package com.example.agoratestandroid.scenes.login

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
import com.example.agoratestandroid.databinding.SceneLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.scene_login) {

    private val binding: SceneLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindLoginButton()
    }

    private fun bindLoginButton() {
        binding.loginBt.setOnClickListener {
            viewModel.login(binding.usernameEt.text.toString())
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoginSuccessFlow.collectLatest {
                    if (it) findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    else Log.e(TAG, "login failed")
                }
            }
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }

}