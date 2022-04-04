package com.example.agoratestandroid.common.chatManager

import io.agora.rtm.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ChatRtmListener : RtmClientListener {

    private val _receivedMessageFlow =
        MutableSharedFlow<String>(extraBufferCapacity = 1)

    val receivedMessageFlow: Flow<String> = _receivedMessageFlow.asSharedFlow()

    override fun onConnectionStateChanged(p0: Int, p1: Int) {

    }

    override fun onMessageReceived(p0: RtmMessage?, p1: String?) {
        _receivedMessageFlow.tryEmit(p0!!.text)
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
}