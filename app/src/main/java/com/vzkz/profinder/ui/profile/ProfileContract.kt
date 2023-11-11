package com.vzkz.profinder.ui.profile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State


data class ProfileState(
    val logout: Boolean
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            logout = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ProfileIntent: Intent {
    data object Logout: ProfileIntent()
}