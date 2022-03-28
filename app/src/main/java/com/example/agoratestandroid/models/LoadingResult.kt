package com.example.agoratestandroid.models

sealed class LoadingResult<out T> {
    object Loading : LoadingResult<Nothing>()
    class Failure(val throwable: Throwable) : LoadingResult<Nothing>()
    class Success<T>(val data: T) : LoadingResult<T>()
    object Empty : LoadingResult<Nothing>()
}
