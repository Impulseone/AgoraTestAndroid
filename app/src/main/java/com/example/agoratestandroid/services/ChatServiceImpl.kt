package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.ActionCallback
import com.example.agoratestandroid.common.chatManager.ChatRtmListener
import com.example.agoratestandroid.common.chatManager.DefaultRtmClientListener
import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import io.agora.rtm.SendMessageOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatServiceImpl(private val rtmClientManager: RtmClientManager) : ChatService {
    override fun sendPeerMessage(peerMessage: PeerMessage) = callbackFlow {
        trySend(LoadingResult.Loading)
        val rtmMessage = rtmClientManager.rtmClient.createMessage()
        rtmMessage.text = peerMessage.text
        val chatManagerCallback = ActionCallback({
            trySend(LoadingResult.Success(true))
        }, {
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })

        rtmClientManager.rtmClient.sendMessageToPeer(
            peerMessage.toId,
            rtmMessage,
            SendMessageOptions(),
            chatManagerCallback
        )
        awaitClose {}
    }

    override fun listenReceivedMessages(rtmClientListener: RtmClientListener) {
        rtmClientManager.registerListener(rtmClientListener)
    }

    override fun stopListeningMessages(rtmClientListener: RtmClientListener) {
        rtmClientManager.removeListener(rtmClientListener)
    }

}