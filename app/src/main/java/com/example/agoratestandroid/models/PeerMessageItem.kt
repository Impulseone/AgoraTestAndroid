package com.example.agoratestandroid.models

import io.agora.rtm.RtmFileMessage
import io.agora.rtm.RtmImageMessage

data class PeerMessageItem(
    val isSelf: Boolean,
    val text: String,
    val rtmImageMessage: RtmImageMessage? = null,
    val rtmFileMessage: RtmFileMessage? = null
)