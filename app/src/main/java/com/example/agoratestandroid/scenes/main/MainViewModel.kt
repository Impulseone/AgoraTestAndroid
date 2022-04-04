package com.example.agoratestandroid.scenes.main

import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.common.mvvm.BaseViewModel
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.State
import com.example.agoratestandroid.services.interfaces.AuthService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(private val authService: AuthService) : BaseViewModel() {

    val launchLoginScreen = Command()
    val launchChatScreen = Command()

    private val logoutCommand = Command()
    private val logoutState = State()

    init {
        logoutCommand.flatMapLatestNonNull {
            logout()
        }.launchIn(viewModelScope)
    }

    fun onLogoutClicked(){
        logoutCommand.call()
    }

    fun onChatClicked(){
        viewModelScope
        launchChatScreen.call()
    }

    private fun logout() = authService.logout().onEach { result ->
        when (result) {
            is LoadingResult.Success -> launchLoginScreen.call()
            is LoadingResult.Loading -> loadingState(logoutState)
            is LoadingResult.Failure -> failureState(result.throwable, logoutState)
            is LoadingResult.Empty -> {}
        }
    }.processThrowable()
}