package com.vzkz.profinder.ui.profile

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.ui.UiText


data class ProfileState(
    val logout: Boolean,
    val user: ActorModel?,
    val error: UiText?,
    val loading: Boolean
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            logout = false,
            user = null,
            error = null,
            loading = true
        )
    }
}

sealed class ProfileIntent : IndividualChatntent {
    data object Logout : ProfileIntent()
    data class SetUser(val user: ActorModel?) : ProfileIntent()
    data class Error(val error: UiText?) : ProfileIntent()
    data class SetState(val state: ProfState) : ProfileIntent()
    data object CloseError: ProfileIntent()
}