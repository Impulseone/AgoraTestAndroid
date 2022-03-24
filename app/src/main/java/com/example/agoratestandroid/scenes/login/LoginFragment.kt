package com.example.agoratestandroid.scenes.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.SceneLoginBinding
import com.example.agoratestandroid.rtmtutorial.App
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback

class LoginFragment : Fragment(R.layout.scene_login) {

    private val binding: SceneLoginBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLoginButton()
    }

    private fun doLogin() {
        App.instance.chatManager.rtmClient.login(
            null,
            binding.usernameEt.text.toString(),
            object : ResultCallback<Void?> {
                override fun onSuccess(responseInfo: Void?) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    Log.e(TAG, "login failed: " + errorInfo.errorCode)
                }
            })
    }

    private fun bindLoginButton() {
        binding.loginBt.setOnClickListener {
            doLogin()
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }
}