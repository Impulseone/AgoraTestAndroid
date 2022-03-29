package com.example.agoratestandroid.scenes.personalChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.common.chatManager.ChatRtmListener
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PersonalChatViewModel(private val chatService: ChatService) : ViewModel() {

    val isSendMessageSuccessFlow = MutableSharedFlow<LoadingResult<String>>()
    val receiveMessageFlow: Flow<LoadingResult<String>>

    private val chatRtmListener = ChatRtmListener()

    init {
        chatService.listenReceivedMessages(chatRtmListener)
        receiveMessageFlow = chatRtmListener.textMessageFlow()
    }

    fun onClickSendPeerMsg(fromId: String, toId: String, text: String) {
        viewModelScope.launch {
            chatService.sendPeerMessage(PeerMessage(fromId, toId, text)).collectLatest {
                isSendMessageSuccessFlow.emit(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatService.stopListeningMessages(chatRtmListener)
    }
}