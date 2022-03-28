package com.example.agoratestandroid.scenes.personalChat

import androidx.lifecycle.ViewModel
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService

class PersonalChatViewModel(private val chatService: ChatService) : ViewModel() {

    fun onClickSendPeerMsg(fromId: String, toId: String, text: String) {
        chatService.sendPeerMessage(PeerMessage(fromId, toId, text))
    }
}