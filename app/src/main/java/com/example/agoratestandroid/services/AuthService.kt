package com.example.agoratestandroid.services

import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun login(username: String): Flow<Boolean>
    fun logout(): Flow<Boolean>
}