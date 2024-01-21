package com.vzkz.profinder.ui.profile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState


data class ProfileState(
    val logout: Boolean,
    val user: ActorModel?,
    val error: Error,
    val loading: Boolean
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            logout = false,
            user = null,
            error = Error(isError = false, errorMsg = null),
            loading = true
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ProfileIntent : Intent {
    data object Logout : ProfileIntent()
    data class SetUser(val user: ActorModel?) : ProfileIntent()
    data class Error(val errorMsg: String) : ProfileIntent()
    data class SetState(val state: ProfState) : ProfileIntent()
}