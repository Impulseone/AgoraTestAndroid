package com.example.agoratestandroid.services.interfaces

import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import kotlinx.coroutines.flow.Flow

interface ChatService {
    fun sendPeerMessage(peerMessage: PeerMessage): Flow<LoadingResult<Boolean>>
}