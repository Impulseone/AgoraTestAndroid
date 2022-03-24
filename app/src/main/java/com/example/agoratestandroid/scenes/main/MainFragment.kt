package com.example.agoratestandroid.scenes.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.SceneMainBinding
import com.example.agoratestandroid.rtmtutorial.App
import com.example.agoratestandroid.scenes.login.LoginFragment
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback

class MainFragment : Fragment(R.layout.scene_main) {
    private val binding: SceneMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLogoutButton()
    }

    private fun bindLogoutButton() {
        binding.logoutBt.setOnClickListener {
            App.instance.chatManager.rtmClient.logout(object : ResultCallback<Void?> {
                override fun onSuccess(p0: Void?) {
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    Log.e(TAG, "logout failed: " + errorInfo.errorCode)
                }
            })
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }
}