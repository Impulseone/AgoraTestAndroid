package com.example.agoratestandroid.scenes.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.databinding.SceneMainBinding
import com.example.agoratestandroid.chatManager.ChatManager
import com.example.agoratestandroid.scenes.login.LoginFragment
import org.koin.android.ext.android.inject

class MainFragment : Fragment(R.layout.scene_main) {

    private val chatManager: ChatManager by inject()
    private val binding: SceneMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindLogoutButton()
    }

    private fun bindLogoutButton() {
        binding.logoutBt.setOnClickListener {
            chatManager.logout({
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }, {
                Log.e(TAG, "logout failed: " + it.errorCode)
            })
        }
    }

    companion object {
        private val TAG: String = LoginFragment::class.java.simpleName
    }
}