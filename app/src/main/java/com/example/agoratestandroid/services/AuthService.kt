package com.example.agoratestandroid.services

import com.example.agoratestandroid.models.LoadingResult
import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun login(username: String): Flow<LoadingResult<Boolean>>
    fun logout(): Flow<LoadingResult<Boolean>>
}