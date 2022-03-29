package com.example.agoratestandroid.common.chatManager

import android.content.Context
import android.util.Log
import com.example.agoratestandroid.R
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.PeerMessage
import io.agora.rtm.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
class ChatManager(mContext: Context) {
    private val rtmClient: RtmClient
    private val messageCallbackListener = MessageCallbackListener()

    init {
        val appID = mContext.getString(R.string.app_id)
        try {
            rtmClient = RtmClient.createInstance(mContext, appID, messageCallbackListener)
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
            val callback = ActionCallback({
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
        val callback = ActionCallback({
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
        val chatManagerCallback = ActionCallback({
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

    fun receiveMessageFlow(): Flow<LoadingResult<String>> = messageCallbackListener.textMessageFlow()

    companion object {
        private val TAG = ChatManager::class.java.simpleName
    }
}