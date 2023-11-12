package com.vzkz.profinder.ui.signup

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.UserModel


data class SignUpState(
    val loading: Boolean,
    val user: UserModel?,
    val success: Boolean,
    val error: Error
) : State {
    companion object {
        val initial: SignUpState = SignUpState(
            loading = false,
            user = null,
            success = false,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SignUpIntent: Intent {
    data class Loading(val isLoading: Boolean): SignUpIntent()
    data class SignUp(val user: UserModel): SignUpIntent()
    data class Error(val errorMsg: String): SignUpIntent()
    data object CloseError: SignUpIntent()
}