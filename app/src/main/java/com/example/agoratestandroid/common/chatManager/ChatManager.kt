package com.example.agoratestandroid.common.chatManager

import android.content.Context
import android.util.Log
import com.example.agoratestandroid.R
import io.agora.rtm.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ChatManager(mContext: Context) {
    private val rtmClient: RtmClient

    init {
        val appID = mContext.getString(R.string.app_id)
        try {
            rtmClient = RtmClient.createInstance(mContext, appID, object : RtmClientListener {
                override fun onConnectionStateChanged(state: Int, reason: Int) {
                }

                override fun onMessageReceived(rtmMessage: RtmMessage, peerId: String) {
                    val text = "Message received from $peerId. Message: ${rtmMessage.text}"
                    Log.e("ChatManager", text)
                }

                override fun onImageMessageReceivedFromPeer(
                    rtmImageMessage: RtmImageMessage,
                    peerId: String
                ) {
                }

                override fun onFileMessageReceivedFromPeer(
                    rtmFileMessage: RtmFileMessage,
                    s: String
                ) {
                }

                override fun onMediaUploadingProgress(
                    rtmMediaOperationProgress: RtmMediaOperationProgress,
                    l: Long
                ) {
                }

                override fun onMediaDownloadingProgress(
                    rtmMediaOperationProgress: RtmMediaOperationProgress,
                    l: Long
                ) {
                }

                override fun onTokenExpired() {}
                override fun onPeersOnlineStatusChanged(status: Map<String, Int>) {}
            })
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            throw RuntimeException(
                "NEED TO check rtm sdk init fatal error ${Log.getStackTraceString(e)}"
            )
        }
    }

    fun login(username: String) =
        callbackFlow {
            trySend(LoadingResult.Loading)
            val callback = ChatManagerCallback({
                trySend(LoadingResult.Success(true))
            }, {
                trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
            })
            rtmClient.login(
                null,
                username,
                callback
            )
            awaitClose {}
        }

    fun logout() = callbackFlow {
        trySend(LoadingResult.Loading)
        val callback = ChatManagerCallback({
            trySend(LoadingResult.Success(true))
        }, {
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })
        rtmClient.logout(callback)
        awaitClose {}
    }

    fun sendPeerMessage(peerMessage: PeerMessage) = callbackFlow {
        trySend(LoadingResult.Loading)
        val rtmMessage = rtmClient.createMessage()
        rtmMessage.text = peerMessage.text
        val chatManagerCallback = ChatManagerCallback({
            trySend(LoadingResult.Success(true))
        }, {
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })

        rtmClient.sendMessageToPeer(
            peerMessage.toId,
            rtmMessage,
            SendMessageOptions(),
            chatManagerCallback
        )
        awaitClose {}
    }

    companion object {
        private val TAG = ChatManager::class.java.simpleName
    }
}