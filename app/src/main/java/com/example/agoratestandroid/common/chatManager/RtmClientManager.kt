package com.example.agoratestandroid.common.chatManager

import android.content.Context
import com.example.agoratestandroid.BuildConfig
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener

class RtmClientManager(context: Context) {
    private val defaultRtmClientListener = DefaultRtmClientListener()

    val rtmClient: RtmClient = RtmClient.createInstance(context, BuildConfig.APP_ID, defaultRtmClientListener)

    fun registerListener(rtmClientListener: RtmClientListener){
        defaultRtmClientListener.registerListener(rtmClientListener)
    }

    fun removeListener(rtmClientListener: RtmClientListener){
        defaultRtmClientListener.removeListener(rtmClientListener)
    }
}