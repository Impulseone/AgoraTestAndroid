package com.example.agoratestandroid.common.mvvm

import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.App
import com.example.agoratestandroid.common.SingleEvent
import com.example.agoratestandroid.models.State
import kotlinx.coroutines.flow.*

abstract class BaseViewModel : AndroidViewModel(App.instance) {
    val showToastCommand = TCommand<String?>()
    private val statesList: MutableList<State> = mutableListOf()
    private val stateMachineFlow = DataList<State>()

    val mainLayoutVisible = Visible()
    val progressVisible = Visible()
    val errorVisible = Visible()

    init {
        stateMachineFlow.setValue(statesList)
        stateMachineFlow.onEach { stateList ->
            if (stateList.data.isNotEmpty()) {
                when {
                    stateList.data.any { it.isError } -> {
                        stateList.data.first { it.isError }.errorMsg?.let { failureResult(it) }
                    }
                    stateList.data.any { it.isLoading } -> loadingResult()
                    stateList.data.all { it.isSuccess } -> successResult()
                }
            }
        }.catch {
            Log.e("BaseViewModel", "stateMachineFlow error: $it")
        }.launchIn(viewModelScope)
    }

    private fun successResult() {
        mainLayoutVisible.setValue(true)
        progressVisible.setValue(false)
        errorVisible.setValue(false)
    }

    private fun loadingResult() {
        mainLayoutVisible.setValue(false)
        progressVisible.setValue(true)
        errorVisible.setValue(false)
    }

    private fun failureResult(msg: Throwable) {
        Log.d("Error", "failureResult: $msg")
        mainLayoutVisible.setValue(false)
        progressVisible.setValue(false)
        errorVisible.setValue(true)
    }

    protected fun <T> Flow<SingleEvent<T>>.flatMapLatestNonNull(block: (SingleEvent<T>) -> Flow<Any>) =
        this.filter { it.data != null }.flatMapLatest {
            block(it)
        }

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        this.getApplication<Application>().getString(resId, *formatArgs)

    protected fun Text(text: String = ""): ViewModelStateFlow<String> = ViewModelStateFlowImpl(text)

    protected fun NullableText(text: String? = null): MutableStateFlow<String?> = MutableStateFlow(text)

    protected fun Visible(isVisible: Boolean = false): ViewModelStateFlow<Boolean> =
        ViewModelStateFlowImpl(isVisible)

    protected fun <T> TCommand(value: T? = null): ViewModelStateFlow<T?> =
        ViewModelStateFlowImpl(value)

    protected fun Command(): ViewModelStateFlow<Unit?> = ViewModelStateFlowImpl(null)

    protected fun <T> DataList(list: List<T> = emptyList()): ViewModelStateFlow<List<T>> =
        ViewModelStateFlowImpl(list)

    protected fun ViewModelStateFlow<Unit?>.call() = when (this) {
        is ViewModelStateFlowImpl -> wrapped.value = SingleEvent(Unit)
    }

    protected fun <T> ViewModelStateFlow<T>.setValue(value: T) = when (this) {
        is ViewModelStateFlowImpl -> wrapped.value = SingleEvent(value)
    }

    protected fun failureState(msg: Throwable, state: State) {
        state.apply {
            isError = true
            isLoading = false
            isSuccess = false
            errorMsg = msg
        }
        if (state !in statesList) statesList.add(state)
        stateMachineFlow.setValue(statesList)
    }

    protected fun loadingState(state: State) {
        state.apply {
            isError = false
            isLoading = true
            isSuccess = false
            errorMsg = null
        }
        if (state !in statesList) statesList.add(state)
        stateMachineFlow.setValue(statesList)
    }

    protected fun successState(state: State) {
        state.apply {
            isError = false
            isLoading = false
            isSuccess = true
            errorMsg = null
        }
        if (state !in statesList) statesList.add(state)
        stateMachineFlow.setValue(statesList)
    }

    protected fun <T> Flow<T>.processThrowable() = this.catch {
        Log.d("Error", "processThrowable: $it")
        failureResult(it.fillInStackTrace())
    }
}

sealed class ViewModelStateFlow<T>(stateFlow: StateFlow<SingleEvent<T>>) :
    StateFlow<SingleEvent<T>> by stateFlow

private class ViewModelStateFlowImpl<T>(
    initial: T, val wrapped: MutableStateFlow<SingleEvent<T>> = MutableStateFlow(
        SingleEvent(initial)
    )
) : ViewModelStateFlow<T>(wrapped)