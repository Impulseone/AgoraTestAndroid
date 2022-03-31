package com.example.agoratestandroid.common.chatManager

import io.agora.rtm.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ChatRtmListener : RtmClientListener {

    private val receivedMessageFlow =
        MutableSharedFlow<String>(extraBufferCapacity = 1)

    override fun onConnectionStateChanged(p0: Int, p1: Int) {

    }

    override fun onMessageReceived(p0: RtmMessage?, p1: String?) {
        receivedMessageFlow.tryEmit(p0!!.text)
    }

    override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {

    }

    override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {

    }

    override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {

    }

    override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {

    }

    override fun onTokenExpired() {

    }

    override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {

    }

    fun receivedMessageFlow(): Flow<String> = receivedMessageFlow
}