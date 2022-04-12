package com.example.agoratestandroid.models

import java.io.File

data class PeerMessageItem(
    val isSelf: Boolean,
    val text: String,
    val file: File? = null
)