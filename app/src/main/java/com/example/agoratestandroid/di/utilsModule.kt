package com.example.agoratestandroid.di

import com.example.agoratestandroid.common.utils.FileUtil
import com.example.agoratestandroid.common.utils.ImageUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single { FileUtil(androidContext()) }
    single { ImageUtils(androidContext()) }
}
