package com.example.agoratestandroid.services

import com.example.agoratestandroid.chatManager.ChatManager
import kotlinx.coroutines.flow.*

class AuthServiceImpl(private val chatManager: ChatManager) : AuthService {
    override fun login(username: String): Flow<Boolean> = chatManager.login(username)
    override fun logout(): Flow<Boolean> = chatManager.logout()
}