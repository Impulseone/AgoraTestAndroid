package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.ChatManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService
import io.agora.rtm.RtmClientListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class ChatServiceImpl(private val chatManager: ChatManager) : ChatService {
    override fun sendPeerMessage(peerMessage: PeerMessage) =
        chatManager.sendPeerMessage(peerMessage)

    override fun listenReceivedMessages(): Flow<LoadingResult<String>> =
        chatManager.receiveMessageFlow()

}