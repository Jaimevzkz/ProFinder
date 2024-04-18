package com.vzkz.profinder.ui.signup

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.UiText


data class SignUpState(
    val loading: Boolean,
    val user: ActorModel?,
    val success: Boolean,
    val error: UiText?
) : State {
    companion object {
        val initial: SignUpState = SignUpState(
            loading = false,
            user = null,
            success = false,
            error = null
        )
    }
}

sealed class SignUpIntent: IndividualChatntent {
    data class Loading(val isLoading: Boolean): SignUpIntent()
    data class SignUp(val user: ActorModel): SignUpIntent()
    data class Error(val error: UiText): SignUpIntent()
    data object CloseError: SignUpIntent()
}