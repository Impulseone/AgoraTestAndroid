@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.agoratestandroid.services

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.interfaces.AttachmentService
import io.agora.rtm.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutionException


class AttachmentServiceImpl(
    private val context: Context,
    private val rtmClientManager: RtmClientManager
) : AttachmentService {
    override fun sendImageMessage(
        peerId: String,
        filePath: String
    ) = callbackFlow {
        trySend(LoadingResult.Loading)
        rtmClientManager.rtmClient.createImageMessageByUploading(
            filePath,
            RtmRequestId(),
            object : ResultCallback<RtmImageMessage?> {
                override fun onSuccess(rtmImageMessage: RtmImageMessage?) {
                    sendRtmImageMessage(rtmImageMessage, this@callbackFlow, peerId, filePath)
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
                    rtmFileMessage?.apply {
                        thumbnail = file.readBytes()
                        rtmClientManager.rtmClient.sendMessageToPeer(
                            peerId,
                            rtmFileMessage,
                            SendMessageOptions(),
                            object : ResultCallback<Void?> {
                                override fun onSuccess(aVoid: Void?) {
                                    trySend(LoadingResult.Success(rtmFileMessage))
                                }

                                override fun onFailure(errorInfo: ErrorInfo?) {
                                    trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                                }
                            })
                    }
                }

                override fun onFailure(errorInfo: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(errorInfo?.errorDescription)))
                }
            })
        awaitClose()
    }

    private fun sendRtmImageMessage(
        rtmImageMessage: RtmImageMessage?,
        scope: ProducerScope<LoadingResult<RtmImageMessage>>,
        peerId: String,
        filePath: String
    ) {
        rtmImageMessage?.apply {
            val configuredImage = configImage(
                this,
                filePath
            )
            rtmClientManager.rtmClient.sendMessageToPeer(
                peerId,
                configuredImage,
                SendMessageOptions(),
                sendMessageCallback(scope, configuredImage)
            )
        }
    }

    private fun sendMessageCallback(
        scope: ProducerScope<LoadingResult<RtmImageMessage>>,
        rtmImageMessage: RtmImageMessage,
    ): ResultCallback<Void?> {
        return object : ResultCallback<Void?> {
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
    }

    private fun configImage(rtmImageMessage: RtmImageMessage, filePath: String): RtmImageMessage {
        val width = rtmImageMessage.width / 5
        val height = rtmImageMessage.width / 5
        rtmImageMessage.thumbnail =
            preloadImage(
                context,
                filePath,
                width,
                height
            )
        rtmImageMessage.thumbnailWidth = width
        rtmImageMessage.thumbnailHeight = height
        return rtmImageMessage
    }

    private fun preloadImage(
        context: Context?,
        filePath: String?,
        width: Int,
        height: Int
    ): ByteArray? {
        try {
            val bitmap =
                Glide.with(context!!).asBitmap().encodeQuality(50).load(filePath)
                    .submit(width, height)
                    .get()
            return bitmapToByteArray(bitmap)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        return baos.toByteArray()
    }

}