package com.example.agoratestandroid

import android.app.Application
import com.example.agoratestandroid.di.rtmClientModule
import com.example.agoratestandroid.di.servicesModule
import com.example.agoratestandroid.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            koin.loadModules(
                listOf(
                    rtmClientModule,
                    viewModelModule,
                    servicesModule
                )
            )
        }
    }
}