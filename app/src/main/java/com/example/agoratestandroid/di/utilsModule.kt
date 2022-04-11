package com.example.agoratestandroid.di

import com.example.agoratestandroid.common.utils.FileUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single { FileUtils(androidContext()) }
}
