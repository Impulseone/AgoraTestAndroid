package com.example.agoratestandroid.services.interfaces

import com.example.agoratestandroid.models.LoadingResult
import io.agora.rtm.RtmImageMessage
import kotlinx.coroutines.flow.Flow

interface AttachmentService {
    fun sendImageMessage(peerId: String, filePath: String): Flow<LoadingResult<RtmImageMessage>>
}