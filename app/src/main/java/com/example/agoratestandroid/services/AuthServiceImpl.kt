package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.ActionCallback
import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.interfaces.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthServiceImpl(private val rtmClientManager: RtmClientManager) : AuthService {
    override fun login(username: String): Flow<LoadingResult<Boolean>> = callbackFlow {
        trySend(LoadingResult.Loading)
        val callback = ActionCallback({
            trySend(LoadingResult.Success(true))
        }, {
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })
        rtmClientManager.rtmClient.login(
            null,
            username,
            callback
        )
        awaitClose {}
    }

    override fun logout(): Flow<LoadingResult<Boolean>> = callbackFlow {
        trySend(LoadingResult.Loading)
        val callback = ActionCallback({
            trySend(LoadingResult.Success(true))
        }, {
            trySend(LoadingResult.Failure(Throwable(it?.errorDescription)))
        })
        rtmClientManager.rtmClient.logout(callback)
        awaitClose {}
    }
}