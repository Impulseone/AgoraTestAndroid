package com.example.agoratestandroid.common.chatManager

import com.example.agoratestandroid.models.LoadingResult
import io.agora.rtm.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class MessageCallbackListener : RtmClientListener {

    private val textMessageFlow = MutableSharedFlow<LoadingResult<String>>(extraBufferCapacity = 1)

    override fun onConnectionStateChanged(p0: Int, p1: Int) {

    }

    override fun onMessageReceived(message: RtmMessage, uid: String) {
        textMessageFlow.tryEmit(LoadingResult.Success(message.text))
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

    fun textMessageFlow(): Flow<LoadingResult<String>> = textMessageFlow
}