package com.example.agoratestandroid.common.chatManager

import com.example.agoratestandroid.models.LoadingResult
import io.agora.rtm.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ChatRtmListener: RtmClientListener {

    private val textMessageFlow = MutableSharedFlow<LoadingResult<String>>(extraBufferCapacity = 1)

    override fun onConnectionStateChanged(p0: Int, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun onMessageReceived(p0: RtmMessage?, p1: String?) {
        textMessageFlow.tryEmit(LoadingResult.Success(p0!!.text))
    }

    override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        TODO("Not yet implemented")
    }

    override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        TODO("Not yet implemented")
    }

    override fun onTokenExpired() {
        TODO("Not yet implemented")
    }

    override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
        TODO("Not yet implemented")
    }

    fun textMessageFlow(): Flow<LoadingResult<String>> = textMessageFlow
}