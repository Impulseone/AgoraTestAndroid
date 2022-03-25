package com.example.agoratestandroid.di

import com.example.agoratestandroid.services.AuthService
import com.example.agoratestandroid.services.AuthServiceImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthService> { AuthServiceImpl(get()) }
}