package com.example.agoratestandroid.scenes.personalChat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.extensions.collectFlow
import com.example.agoratestandroid.common.extensions.showToast
import com.example.agoratestandroid.databinding.ScenePersonalChatBinding
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerItemMessage
import com.example.agoratestandroid.scenes.personalChat.adapter.MessagesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalChatFragment : Fragment(R.layout.scene_personal_chat) {
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val viewModel: PersonalChatViewModel by viewModel()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    private val messagesAdapter = MessagesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        bindTitle()
        bindSendMessageBtn()
        bindAdapter()
    }

    private fun bindViewModel() {
        with(viewModel) {
            collectFlow(isSendMessageSuccessFlow) {
                when (it) {
                    is LoadingResult.Loading -> {}
                    is LoadingResult.Success -> messagesAdapter.update(
                        PeerItemMessage(
                            isSelf = true,
                            it.data
                        )
                    )
                    is LoadingResult.Failure -> {
                        showToast(it.throwable.message)
                    }
                    is LoadingResult.Empty -> {
                        showToast("loading result is empty")
                    }
                }
            }
            collectFlow(receiveMessageFlow) {
                when (it) {
                    is LoadingResult.Success -> messagesAdapter.update(
                        PeerItemMessage(
                            isSelf = false,
                            it.data
                        )
                    )
                    is LoadingResult.Failure -> {
                        showToast(it.throwable.message)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun bindTitle() {
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

    private fun bindAdapter() {
        binding.messagesRv.adapter = messagesAdapter
    }
}