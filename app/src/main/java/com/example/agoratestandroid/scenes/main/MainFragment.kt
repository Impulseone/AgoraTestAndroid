package com.example.agoratestandroid.scenes.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.extensions.collectFlow
import com.example.agoratestandroid.common.extensions.showToast
import com.example.agoratestandroid.databinding.SceneMainBinding
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.scenes.login.LoginFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.scene_main) {

    private val binding: SceneMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()
    private val navArgs by navArgs<MainFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindLogoutButton()
        bindChatButton()
    }

    private fun bindLogoutButton() {
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun bindChatButton() {
        binding.chatBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_personalChatFragment,
                bundleOf(
                    USER_ID to navArgs.userId,
                    PEER_ID to binding.friendNameEt.text.toString()
                )
            )
        }
    }

    private fun bindViewModel() {
        collectFlow(viewModel.isLogoutSuccessFlow) {
            when (it) {
                is LoadingResult.Loading -> {
                    showLoading(true)
                }
                is LoadingResult.Success -> findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                is LoadingResult.Failure -> {
                    showLoading(false)
                    showToast(it.throwable.message)
                    Log.e(TAG, "${it.throwable.message}")
                }
                is LoadingResult.Empty -> {
                    showLoading(false)
                    Log.e(TAG, "logout result is empty")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            progressbar.isVisible = isLoading
            logoutBtn.isVisible = !isLoading
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
        private const val USER_ID: String = "userId"
        private const val PEER_ID: String = "peerId"
    }
}