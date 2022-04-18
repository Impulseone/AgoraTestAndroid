package com.example.agoratestandroid.di

import com.example.agoratestandroid.services.AttachmentServiceImpl
import com.example.agoratestandroid.services.AuthServiceImpl
import com.example.agoratestandroid.services.ChatServiceImpl
import com.example.agoratestandroid.services.interfaces.AttachmentService
import com.example.agoratestandroid.services.interfaces.AuthService
import com.example.agoratestandroid.services.interfaces.ChatService
import org.koin.dsl.module

val servicesModule = module {
    single<AuthService> { AuthServiceImpl(get()) }
    single<ChatService> { ChatServiceImpl(get()) }
    single<AttachmentService> { AttachmentServiceImpl(get(), get()) }
}