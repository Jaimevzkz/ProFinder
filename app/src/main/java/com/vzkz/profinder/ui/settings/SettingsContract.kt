package com.vzkz.profinder.ui.settings

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State


data class SettingsState(
    val darkTheme: Boolean,
) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            darkTheme = false
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class SettingsIntent: IndividualChatntent {
    data object Theme: SettingsIntent()
    data class Init(val theme: Boolean): SettingsIntent()
}