package com.example.agoratestandroid.scenes.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.bind
import com.example.agoratestandroid.common.bindAction
import com.example.agoratestandroid.common.bindTextTwoWay
import com.example.agoratestandroid.common.extensions.hideKeyboard
import com.example.agoratestandroid.common.extensions.onDone
import com.example.agoratestandroid.common.mvvm.BaseFragment
import com.example.agoratestandroid.common.onClickListener
import com.example.agoratestandroid.databinding.SceneMainBinding
import com.example.agoratestandroid.scenes.navigation.Navigator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainViewModel>(R.layout.scene_main) {

    private val binding: SceneMainBinding by viewBinding()
    private val navArgs by navArgs<MainFragmentArgs>()

    override val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    private fun initViews() {
        with(viewModel) {
            with(binding) {
                friendNameEt.onDone {
                    hideKeyboard()
                    onChatClicked()
                }
                onClickListener(logoutBtn) { onLogoutClicked() }
                onClickListener(chatBtn) { onChatClicked() }
            }
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        with(binding) {
            with(viewModel) {
                bindTextTwoWay(friendName, friendNameEt)
                bind(errorText) { friendNameErrorTv.text = it }
                bindAction(launchLoginScreen) {
                    Navigator.goToLoginScreen(this@MainFragment)
                }
                bindAction(launchChatScreen) {
                    if (friendNameEt.text.isNotEmpty())
                        Navigator.goToChatScreen(
                            this@MainFragment,
                            navArgs.userId,
                            friendNameEt.text.toString()
                        )
                }
            }
        }
    }
}