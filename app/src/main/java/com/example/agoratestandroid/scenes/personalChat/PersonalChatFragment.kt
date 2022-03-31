package com.example.agoratestandroid.scenes.personalChat

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.bind
import com.example.agoratestandroid.common.bindRecyclerViewAdapter
import com.example.agoratestandroid.common.mvvm.BaseFragment
import com.example.agoratestandroid.databinding.ScenePersonalChatBinding
import com.example.agoratestandroid.models.PeerMessageItem
import com.example.agoratestandroid.scenes.personalChat.adapter.MessagesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalChatFragment : BaseFragment<PersonalChatViewModel>(R.layout.scene_personal_chat) {
    private val binding: ScenePersonalChatBinding by viewBinding()
    private val navArgs by navArgs<PersonalChatFragmentArgs>()

    private val messagesAdapter = MessagesAdapter()

    override val viewModel: PersonalChatViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    private fun initViews() {
        with(binding) {
            with(viewModel) {
                friendId.text = navArgs.peerId
                messagesRv.adapter = messagesAdapter
                sendPeerMsgButton.setOnClickListener {
                    val fromId = navArgs.userId
                    val toId = navArgs.peerId
                    val messageText = binding.messageEt.text.toString()
                    onClickSendPeerMsg(fromId, toId, messageText)
                }
            }
        }
    }

    override fun bindViewModel() {
        with(binding) {
            with(viewModel) {
                bindRecyclerViewAdapter(messagesList, messagesAdapter) {
                    messagesRv.scrollToPosition(messagesList.value.data.size - 1)
                    if (messagesList.value.data.isNotEmpty() && messagesList.value.data.last().isSelf) messageEt.text.clear()
                }
            }
        }
    }
}