package com.example.agoratestandroid.scenes.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.SceneLoginBinding
import com.example.agoratestandroid.chatManager.ChatManager
import org.koin.android.ext.android.inject

class LoginFragment : Fragment(R.layout.scene_login) {

    private val chatManager: ChatManager by inject()
    private val binding: SceneLoginBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLoginButton()
    }

    private fun bindLoginButton() {
        binding.loginBt.setOnClickListener {
            chatManager.login(
                binding.usernameEt.text.toString(),
                { findNavController().navigate(R.id.action_loginFragment_to_mainFragment) },
                { Log.e(TAG, "login failed: " + it.errorCode) })
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }
}