package com.example.agoratestandroid.scenes.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.AuthService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    val isLoginSuccessFlow = MutableSharedFlow<LoadingResult<Boolean>>()

    fun login(username: String) {
        viewModelScope.launch {
            authService.login(username).collectLatest {
                isLoginSuccessFlow.emit(it)
            }
        }
    }

}