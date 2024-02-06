package com.vzkz.profinder.ui.login

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError


data class LoginState(
    val loading: Boolean,
    val user: ActorModel?,
    val success: Boolean,
    val error: UiError
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            user = null,
            success = false,
            error = UiError(false, null)
        )
    }
}

sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Login(val user: ActorModel): LoginIntent()
    data class Error(val errorMsg: String): LoginIntent()
    data object CloseError: LoginIntent()
}