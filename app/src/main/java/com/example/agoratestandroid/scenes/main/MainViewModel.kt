package com.example.agoratestandroid.scenes.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.interfaces.AuthService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val authService: AuthService) : ViewModel() {

    val isLogoutSuccessFlow = MutableSharedFlow<LoadingResult<Boolean>>()

    fun logout() {
        viewModelScope.launch {
            authService.logout().collectLatest {
                isLogoutSuccessFlow.emit(it)
            }
        }
    }
}