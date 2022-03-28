package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.ChatManager
import com.example.agoratestandroid.models.LoadingResult
import kotlinx.coroutines.flow.*

class AuthServiceImpl(private val chatManager: ChatManager) : AuthService {
    override fun login(username: String): Flow<LoadingResult<Boolean>> = chatManager.login(username)
    override fun logout(): Flow<LoadingResult<Boolean>> = chatManager.logout()
}