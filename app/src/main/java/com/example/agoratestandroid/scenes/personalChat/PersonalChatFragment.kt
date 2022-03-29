package com.example.agoratestandroid.scenes.personalChat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.extensions.collectFlow
import com.example.agoratestandroid.common.extensions.showSnackbar
import com.example.agoratestandroid.databinding.ScenePersonalChatBinding
import com.example.agoratestandroid.models.LoadingResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalChatFragment : Fragment(R.layout.scene_personal_chat) {
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val viewModel: PersonalChatViewModel by viewModel()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindTitle()
        bindSendMessageBtn()
    }

    private fun bindViewModel() {
        with(viewModel) {
            collectFlow(isSendMessageSuccessFlow) {
                when (it) {
                    is LoadingResult.Loading -> {}
                    is LoadingResult.Success -> showSnackbar("send message success")
                    is LoadingResult.Failure -> {
                        showSnackbar(it.throwable.message)
                    }
                    is LoadingResult.Empty -> {
                        showSnackbar("login result is empty")
                    }
                }
            }
            collectFlow(receiveMessageFlow){
                when(it){
                    is LoadingResult.Success -> showSnackbar(it.data)
                }
            }
        }
    }

    private fun bindTitle(){
        binding.friendId.text = navArgs.peerId
    }

    private fun bindSendMessageBtn() {
        binding.sendPeerMsgButton.setOnClickListener {
            val fromId = navArgs.userId
            val toId = navArgs.peerId
            val messageText = binding.messageEt.text.toString()
            viewModel.onClickSendPeerMsg(fromId, toId, messageText)
        }
    }
}