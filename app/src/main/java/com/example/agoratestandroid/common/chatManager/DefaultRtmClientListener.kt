package com.example.agoratestandroid.common.chatManager

import io.agora.rtm.*

class DefaultRtmClientListener : RtmClientListener {

    private val listenersList = mutableListOf<RtmClientListener>()

    override fun onConnectionStateChanged(p0: Int, p1: Int) {
        listenersList.forEach {
            it.onConnectionStateChanged(p0, p1)
        }
    }

    override fun onMessageReceived(message: RtmMessage, uid: String) {
        listenersList.forEach {
            it.onMessageReceived(message, uid)
        }
    }

    override fun onImageMessageReceivedFromPeer(p0: RtmImageMessage?, p1: String?) {
        listenersList.forEach {
            it.onImageMessageReceivedFromPeer(p0, p1)
        }
    }

    override fun onFileMessageReceivedFromPeer(p0: RtmFileMessage?, p1: String?) {
        listenersList.forEach {
            it.onFileMessageReceivedFromPeer(p0, p1)
        }
    }

    override fun onMediaUploadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        listenersList.forEach {
            it.onMediaUploadingProgress(p0, p1)
        }
    }

    override fun onMediaDownloadingProgress(p0: RtmMediaOperationProgress?, p1: Long) {
        listenersList.forEach {
            it.onMediaDownloadingProgress(p0, p1)
        }
    }

    override fun onTokenExpired() {
        listenersList.forEach {
            it.onTokenExpired()
        }
    }

    override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
        listenersList.forEach {
            it.onPeersOnlineStatusChanged(p0)
        }
    }

    fun registerListener(rtmClientListener: RtmClientListener){
        listenersList.add(rtmClientListener)
    }

    fun removeListener(rtmClientListener: RtmClientListener){
        listenersList.remove(rtmClientListener)
    }
}