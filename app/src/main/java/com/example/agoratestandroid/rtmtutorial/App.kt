package com.example.agoratestandroid.rtmtutorial

import android.app.Application

class App : Application() {
    lateinit var chatManager: ChatManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        chatManager = ChatManager(this)
        chatManager.init()
    }

    companion object {
        lateinit var instance: App
    }
}