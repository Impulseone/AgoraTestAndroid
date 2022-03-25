package com.example.agoratestandroid.common.chatManager

import android.content.Context
import android.util.Log
import com.example.agoratestandroid.BuildConfig
import com.example.agoratestandroid.R
import com.example.agoratestandroid.models.AuthResultCallback
import io.agora.rtm.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import com.example.agoratestandroid.models.LoadingResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class ChatManager(mContext: Context) {
    private val rtmClient: RtmClient
    private val sendMessageOptions: SendMessageOptions
    private val mListenerList: MutableList<RtmClientListener> = mutableListOf()
    private val mMessagePool = RtmMessagePool()

    init {
        val appID = mContext.getString(R.string.app_id)
        try {
            rtmClient = RtmClient.createInstance(mContext, appID, object : RtmClientListener {
                override fun onConnectionStateChanged(state: Int, reason: Int) {
                    for (listener in mListenerList) {
                        listener.onConnectionStateChanged(state, reason)
                    }
                }

                override fun onMessageReceived(rtmMessage: RtmMessage, peerId: String) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                        mMessagePool.insertOfflineMessage(rtmMessage, peerId)
                    } else {
                        for (listener in mListenerList) {
                            listener.onMessageReceived(rtmMessage, peerId)
                        }
                    }
                }

                override fun onImageMessageReceivedFromPeer(
                    rtmImageMessage: RtmImageMessage,
                    peerId: String
                ) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                        mMessagePool.insertOfflineMessage(rtmImageMessage, peerId)
                    } else {
                        for (listener in mListenerList) {
                            listener.onImageMessageReceivedFromPeer(rtmImageMessage, peerId)
                        }
                    }
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
            if (BuildConfig.DEBUG) {
                rtmClient.setParameters("{\"rtm.log_filter\": 65535}")
            }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            throw RuntimeException(
                """NEED TO check rtm sdk init fatal error ${Log.getStackTraceString(e)}""".trimIndent()
            )
        }

        // Global option, mainly used to determine whether
        // to support offline messages now.
        sendMessageOptions = SendMessageOptions()
    }

    fun registerListener(listener: RtmClientListener) {
        mListenerList.add(listener)
    }

    fun unregisterListener(listener: RtmClientListener) {
        mListenerList.remove(listener)
    }

    fun enableOfflineMessage(enabled: Boolean) {
        sendMessageOptions.enableOfflineMessaging = enabled
    }

    val isOfflineMessageEnabled: Boolean
        get() = sendMessageOptions.enableOfflineMessaging

    fun getAllOfflineMessages(peerId: String?): List<RtmMessage>? {
        return mMessagePool.getAllOfflineMessages(peerId!!)
    }

    fun removeAllOfflineMessages(peerId: String?) {
        mMessagePool.removeAllOfflineMessages(peerId!!)
    }

    companion object {
        private val TAG = ChatManager::class.java.simpleName
    }

    fun login(username: String) =
        callbackFlow {
            trySend(LoadingResult.Loading)
            val callback = AuthResultCallback({
                trySend(LoadingResult.Success(true))
            },{
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
        val callback = AuthResultCallback({
            trySend(LoadingResult.Success(true))
        },{
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })
        rtmClient.logout(callback)
        awaitClose {}
    }
}