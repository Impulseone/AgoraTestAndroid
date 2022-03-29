package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClientListener
import io.agora.rtm.SendMessageOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ChatServiceImpl(private val rtmClientManager: RtmClientManager) : ChatService {
    override fun sendPeerMessage(peerMessage: PeerMessage) = callbackFlow {
        trySend(LoadingResult.Loading)
        val rtmMessage = rtmClientManager.rtmClient.createMessage()
        rtmMessage.text = peerMessage.text
        rtmClientManager.rtmClient.sendMessageToPeer(
            peerMessage.toId,
            rtmMessage,
            SendMessageOptions(),
            object : ResultCallback<Void?> {
                override fun onSuccess(p0: Void?) {
                    trySend(LoadingResult.Success(peerMessage.text))
                }
                override fun onFailure(p0: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(p0?.errorDescription)))
                }
            }
        )
        awaitClose {}
    }

    override fun listenReceivedMessages(rtmClientListener: RtmClientListener) = rtmClientManager.registerListener(rtmClientListener)

    override fun stopListeningMessages(rtmClientListener: RtmClientListener) = rtmClientManager.removeListener(rtmClientListener)

}