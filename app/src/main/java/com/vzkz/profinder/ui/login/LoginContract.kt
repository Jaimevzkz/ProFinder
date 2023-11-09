package com.vzkz.profinder.ui.login

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State


data class LoginState(
    val loading: Boolean,
    val userName: String?,
    val success: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            userName = null,
            success = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Login(val username: String): LoginIntent()
    data class Error(val errorMsg: String): LoginIntent()
    data object CloseError: LoginIntent()
}