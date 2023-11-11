package com.vzkz.profinder.ui.signup

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State


data class SignUpState(
    val loading: Boolean,
    val nickname: String?,
    val success: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: SignUpState = SignUpState(
            loading = false,
            nickname = null,
            success = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SignUpIntent: Intent {
    data class Loading(val isLoading: Boolean): SignUpIntent()
    data class SignUp(val username: String): SignUpIntent()
    data class Error(val errorMsg: String): SignUpIntent()
    data object CloseError: SignUpIntent()
}