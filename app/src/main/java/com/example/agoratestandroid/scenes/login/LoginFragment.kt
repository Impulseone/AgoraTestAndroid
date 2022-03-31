package com.example.agoratestandroid.scenes.login

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.bind
import com.example.agoratestandroid.common.bindAction
import com.example.agoratestandroid.common.bindTextTwoWay
import com.example.agoratestandroid.common.bindVisible
import com.example.agoratestandroid.common.extensions.onDone
import com.example.agoratestandroid.common.mvvm.BaseFragment
import com.example.agoratestandroid.databinding.SceneLoginBinding
import com.example.agoratestandroid.scenes.navigation.Navigator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel>(R.layout.scene_login) {

    private val binding: SceneLoginBinding by viewBinding()
    override val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    private fun initViews() {
        with(viewModel) {
            with(binding) {
                usernameEt.onDone {
                    onLoginClicked()
                }
                loginBt.setOnClickListener {
                    onLoginClicked()
                }
            }
        }
    }

    override fun bindViewModel() {
        with(binding) {
            with(viewModel) {
                bindTextTwoWay(username, usernameEt)
                bind(usernameErrorText) { usernameErrorTv.text = it }
                bindVisible(progressVisible, progressBar)
                bindVisible(mainLayoutVisible, mainLayout)
                bindAction(launchMainScreen) { Navigator.goToMainScreen(this@LoginFragment) }
            }
        }
    }
}