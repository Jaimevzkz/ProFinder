package com.vzkz.profinder.ui.login

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.UiText


data class LoginState(
    val loading: Boolean,
    val user: ActorModel?,
    val success: Boolean,
    val error: UiText?
) : State {
    companion object {
        val initial: LoginState = LoginState(
            loading = false,
            user = null,
            success = false,
            error = null
        )
    }
}

sealed class LoginIntent: Intent {
    data class Loading(val isLoading: Boolean): LoginIntent()
    data class Login(val user: ActorModel): LoginIntent()
    data class Error(val error: UiText): LoginIntent()
    data object CloseError: LoginIntent()
}