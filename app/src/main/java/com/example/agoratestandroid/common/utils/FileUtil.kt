package com.example.agoratestandroid.common.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

class FileUtil(private val context: Context) {
    fun createFileFromUri(uri: Uri): File {
        val file = File(context.filesDir, context.getFileName(uri) ?: "")
        val stream = context.contentResolver.openInputStream(uri)
        FileOutputStream(file, false).use { outputStream ->
            var read: Int
            val bytes = ByteArray(DEFAULT_BUFFER_SIZE)
            while (stream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        }
        return file
    }
}

fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

