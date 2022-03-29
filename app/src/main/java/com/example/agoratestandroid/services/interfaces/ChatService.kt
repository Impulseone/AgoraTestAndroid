package com.example.agoratestandroid.services.interfaces

import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import io.agora.rtm.RtmClientListener
import kotlinx.coroutines.flow.Flow

interface ChatService {
    fun sendPeerMessage(peerMessage: PeerMessage): Flow<LoadingResult<String>>
    fun listenReceivedMessages(rtmClientListener: RtmClientListener)
    fun stopListeningMessages(rtmClientListener: RtmClientListener)
}