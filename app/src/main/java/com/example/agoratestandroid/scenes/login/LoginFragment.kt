package com.example.agoratestandroid.scenes.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.extensions.collectFlow
import com.example.agoratestandroid.common.extensions.showSnackbar
import com.example.agoratestandroid.databinding.SceneLoginBinding
import com.example.agoratestandroid.models.LoadingResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.scene_login) {

    private val binding: SceneLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindLoginButton()
    }

    private fun bindViewModel() {
        collectFlow(viewModel.isLoginSuccessFlow) {
            when (it) {
                is LoadingResult.Loading -> showLoading(true)
                is LoadingResult.Success -> findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                is LoadingResult.Failure -> {
                    showLoading(false)
                    showSnackbar(it.throwable.message)
                    Log.e(TAG, "${it.throwable.message}")
                }
                is LoadingResult.Empty -> {
                    showLoading(false)
                    Log.e(TAG, "login result is empty")
                }
            }
        }
    }

    private fun bindLoginButton() {
        binding.loginBt.setOnClickListener {
            viewModel.login(binding.usernameEt.text.toString())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            progressbar.isVisible = isLoading
            loginBt.isVisible = !isLoading
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }

}