package com.example.agoratestandroid.common.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FileUtils(private val context: Context) {
    fun createFileFromUri(uri: Uri): File {
        val image = File(context.filesDir, "${UUID.randomUUID()}.${getFileType(uri)}")
        val stream = context.contentResolver.openInputStream(uri)
        FileOutputStream(image, false).use { outputStream ->
            var read: Int
            val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
            while (stream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        }
        return image
    }

    private fun getFileType(uri: Uri): String {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))!!
    }
}

