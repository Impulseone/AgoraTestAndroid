package com.example.agoratestandroid.scenes.login

import androidx.lifecycle.viewModelScope
import com.example.agoratestandroid.R
import com.example.agoratestandroid.common.mvvm.BaseViewModel
import com.example.agoratestandroid.models.LoadingResult
import com.example.agoratestandroid.models.State
import com.example.agoratestandroid.services.interfaces.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(private val authService: AuthService) : BaseViewModel() {

    val launchMainScreen = Command()
    val loginCommand = Command()

    val username = NullableText()
    val usernameErrorText = Text()

    private val authorisationState = State()

    init {
        username.onEach { username ->
            if (!username.isNullOrEmpty()) usernameErrorText.setValue("")
            else usernameErrorText.setValue(getString(R.string.scr_login_error_username))
        }.launchIn(viewModelScope)

        loginCommand.flatMapLatestNonNull {
            if (!username.value.isNullOrEmpty()) login()
            else emptyFlow()
        }.launchIn(viewModelScope)
    }

    private fun login(): Flow<LoadingResult<Boolean>> = authService.login(username.value!!).onEach { loginResult ->
        when (loginResult) {
            is LoadingResult.Success -> launchMainScreen.call()
            is LoadingResult.Loading -> loadingState(authorisationState)
            is LoadingResult.Failure -> failureState(loginResult.throwable, authorisationState)
            is LoadingResult.Empty -> {}
        }
    }.processThrowable()

    fun onLoginClicked() {
        loginCommand.call()
    }

}