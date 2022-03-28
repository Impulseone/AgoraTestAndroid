package com.example.agoratestandroid.di

import com.example.agoratestandroid.scenes.login.LoginViewModel
import com.example.agoratestandroid.scenes.main.MainViewModel
import com.example.agoratestandroid.scenes.personalChat.PersonalChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { PersonalChatViewModel(get()) }
}