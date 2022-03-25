package com.example.agoratestandroid.models

import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback

class AuthResultCallback(
    private val onSuccessResult: () -> Unit,
    private val onFailureResult: (errorInfo: ErrorInfo?) -> Unit
) :
    ResultCallback<Void?> {
    override fun onSuccess(p0: Void?) {
        onSuccessResult()
    }

    override fun onFailure(errorInfo: ErrorInfo?) {
        onFailureResult(errorInfo)
    }
}