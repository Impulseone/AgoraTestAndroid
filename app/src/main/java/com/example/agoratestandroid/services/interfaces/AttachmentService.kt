package com.example.agoratestandroid.services.interfaces

import com.example.agoratestandroid.models.LoadingResult
import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AttachmentService {

    fun sendImageMessage(peerId: String, filePath: String): Flow<LoadingResult<RtmImageMessage>>

    fun sendFileMessage(peerId: String, file: File): Flow<LoadingResult<RtmFileMessage>>

    fun saveFileToStorage(rtmFileMessage: RtmFileMessage, filePath: String): Flow<LoadingResult<RtmFileMessage>>

}