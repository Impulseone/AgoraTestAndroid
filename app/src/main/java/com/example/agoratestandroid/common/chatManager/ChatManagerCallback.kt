package com.example.agoratestandroid.common.chatManager

import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback

class ChatManagerCallback(
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