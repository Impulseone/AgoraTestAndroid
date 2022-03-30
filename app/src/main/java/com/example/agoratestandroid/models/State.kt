package com.example.agoratestandroid.models

open class State(
    var isSuccess: Boolean = false,
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMsg: Throwable? = null
)