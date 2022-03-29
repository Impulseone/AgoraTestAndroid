package com.example.agoratestandroid.di

import com.example.agoratestandroid.common.chatManager.RtmClientManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val rtmClientModule = module {
    single {RtmClientManager(androidContext())}
}