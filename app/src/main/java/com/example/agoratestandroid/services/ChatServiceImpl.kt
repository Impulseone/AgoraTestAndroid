package com.example.agoratestandroid.services

import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import com.example.agoratestandroid.services.interfaces.ChatService
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.SendMessageOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ChatServiceImpl(private val rtmClient: RtmClient) : ChatService {
    override fun sendPeerMessage(peerMessage: PeerMessage, isPeerOnline: Boolean) = callbackFlow {
        trySend(LoadingResult.Loading)
        val rtmMessage = rtmClient.createMessage()
        rtmMessage.text = peerMessage.text
        rtmClient.sendMessageToPeer(
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

}