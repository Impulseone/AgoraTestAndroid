@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.agoratestandroid.services

import android.util.Log
import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.common.utils.ImageUtils
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.interfaces.AttachmentService
import io.agora.rtm.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.File


class AttachmentServiceImpl(
    private val rtmClientManager: RtmClientManager,
    private val imageUtils: ImageUtils
) : AttachmentService {

    override fun sendImageMessage(
        peerId: String,
        file: File
    ) = callbackFlow {
        trySend(LoadingResult.Loading)
        rtmClientManager.rtmClient.createImageMessageByUploading(
            file.path,
            RtmRequestId(),
            object : ResultCallback<RtmImageMessage?> {
                override fun onSuccess(rtmImageMessage: RtmImageMessage?) {
                    sendRtmImageMessage(rtmImageMessage, this@callbackFlow, peerId, file)
                }

                override fun onFailure(errorInfo: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                }
            })
        awaitClose()
    }

    override fun sendFileMessage(
        peerId: String,
        file: File
    ): Flow<LoadingResult<RtmFileMessage>> = callbackFlow {
        trySend(LoadingResult.Loading)
        val requestId = RtmRequestId()
        rtmClientManager.rtmClient.createFileMessageByUploading(
            file.path,
            requestId,
            object : ResultCallback<RtmFileMessage?> {
                override fun onSuccess(rtmFileMessage: RtmFileMessage?) {
                    rtmFileMessage!!.fileName = file.name
                    sendRtmFileMessage(rtmFileMessage, this@callbackFlow, peerId)
                }

                override fun onFailure(errorInfo: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                }
            })
        awaitClose()
    }

    override fun saveMediaToStorage(
        mediaId: String,
        filePath: String
    ): Flow<LoadingResult<Unit>> = callbackFlow {
        trySend(LoadingResult.Loading)
        val requestId = RtmRequestId()
        rtmClientManager.rtmClient.downloadMediaToFile(
            mediaId,
            filePath,
            requestId,
            object : ResultCallback<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    trySend(LoadingResult.Success(Unit))
                }

                override fun onFailure(errorInfo: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                }
            }
        )
        awaitClose()
    }

    private fun sendRtmImageMessage(
        rtmImageMessage: RtmImageMessage?,
        scope: ProducerScope<LoadingResult<RtmImageMessage>>,
        peerId: String,
        file: File
    ) {
        rtmImageMessage?.apply {
            val configuredImage = imageUtils.configImage(
                this,
                file
            )
            rtmClientManager.rtmClient.sendMessageToPeer(
                peerId,
                configuredImage,
                SendMessageOptions(),
                object : ResultCallback<Void?> {
                    override fun onSuccess(aVoid: Void?) {
                        scope.trySend(
                            LoadingResult.Success(
                                rtmImageMessage
                            )
                        )
                    }

                    override fun onFailure(errorInfo: ErrorInfo) {
                        scope.trySend(LoadingResult.Failure(Throwable(errorInfo.errorDescription)))
                    }
                }
            )
        }
    }

    private fun sendRtmFileMessage(
        rtmFileMessage: RtmFileMessage?,
        scope: ProducerScope<LoadingResult<RtmFileMessage>>,
        peerId: String
    ) {
        rtmFileMessage?.apply {
            rtmClientManager.rtmClient.sendMessageToPeer(
                peerId,
                rtmFileMessage,
                SendMessageOptions(),
                object : ResultCallback<Void?> {
                    override fun onSuccess(aVoid: Void?) {
                        scope.trySend(LoadingResult.Success(rtmFileMessage))
                    }

                    override fun onFailure(errorInfo: ErrorInfo?) {
                        scope.trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                    }
                })
        }
    }

}