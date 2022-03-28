package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.ChatManager
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService

class ChatServiceImpl(private val chatManager: ChatManager) : ChatService {
    override fun sendPeerMessage(peerMessage: PeerMessage) {
        chatManager.sendPeerMessage(peerMessage)
    }
}