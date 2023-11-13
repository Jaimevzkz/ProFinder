package com.vzkz.profinder.ui.profile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.Flow


data class ProfileState(
    val logout: Boolean,
    val user: UserModel?,
    val error: Error
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            logout = false,
            user = null,
            error = Error(isError = false, errorMsg = null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ProfileIntent: Intent {
    data object Logout: ProfileIntent()
    data class SetUserFromDS(val user: UserModel?): ProfileIntent()
    data class Error(val errorMsg: String): ProfileIntent()
}