package com.example.agoratestandroid.di

import com.example.agoratestandroid.BuildConfig
import com.example.agoratestandroid.common.chatManager.ChatRtmListener
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmClientListener
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val rtmClientModule = module {
    single<RtmClientListener> { ChatRtmListener() }
    single(createdAtStart = true) {
        RtmClient.createInstance(
            androidContext(),
            BuildConfig.APP_ID,
            get()
        )
    }
}