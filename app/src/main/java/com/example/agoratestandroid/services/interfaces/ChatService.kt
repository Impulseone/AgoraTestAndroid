package com.example.agoratestandroid.services.interfaces

import com.example.agoratestandroid.models.PeerMessage

interface ChatService {
    fun sendPeerMessage(peerMessage: PeerMessage)
}