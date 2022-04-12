package com.example.agoratestandroid.common.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import io.agora.rtm.RtmImageMessage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutionException

class ImageUtils(private val context: Context) {

    fun configImage(rtmImageMessage: RtmImageMessage, file: File): RtmImageMessage {
        val width = rtmImageMessage.width / 5
        val height = rtmImageMessage.width / 5
        with(rtmImageMessage) {
            thumbnail =
                preloadImage(
                    file.path,
                    width,
                    height
                )
            thumbnailWidth = width
            thumbnailHeight = height
            fileName = file.name
        }
        return rtmImageMessage
    }

    private fun preloadImage(
        filePath: String?,
        width: Int,
        height: Int
    ): ByteArray? {
        try {
            val bitmap =
                Glide.with(context).asBitmap().encodeQuality(50).load(filePath)
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
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}