package com.example.agoratestandroid.di

import com.example.agoratestandroid.common.chatManager.ChatManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val chatManagerModule = module {
    single { ChatManager(androidContext()) }
}