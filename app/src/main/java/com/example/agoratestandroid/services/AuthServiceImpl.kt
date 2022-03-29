package com.example.agoratestandroid.services

import com.example.agoratestandroid.common.chatManager.RtmClientManager
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.services.interfaces.AuthService
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthServiceImpl(private val rtmClientManager: RtmClientManager) : AuthService {
    override fun login(username: String): Flow<LoadingResult<Boolean>> = callbackFlow {
        trySend(LoadingResult.Loading)
        rtmClientManager.rtmClient.login(
            null,
            username,
            object : ResultCallback<Void?> {
                override fun onSuccess(p0: Void?) {
                    trySend(LoadingResult.Success(true))
                }

                override fun onFailure(p0: ErrorInfo?) {
                    trySend(LoadingResult.Failure(Throwable(p0?.errorDescription)))
                }

            }
        )
        awaitClose {}
    }

    override fun logout(): Flow<LoadingResult<Boolean>> = callbackFlow {
        trySend(LoadingResult.Loading)
        rtmClientManager.rtmClient.logout(object : ResultCallback<Void?> {
            override fun onSuccess(p0: Void?) {
                trySend(LoadingResult.Success(true))
            }

            override fun onFailure(p0: ErrorInfo?) {
                trySend(LoadingResult.Failure(Throwable(p0?.errorDescription)))
            }

        })
        awaitClose {}
    }
}