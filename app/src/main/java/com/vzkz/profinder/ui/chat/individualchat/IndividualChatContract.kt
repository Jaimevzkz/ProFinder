package com.vzkz.profinder.ui.chat.individualchat

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.UiError


data class IndividualChatState(
    val loading: Boolean,
    val error: UiError
//    val counter: Int,
) : State {
    companion object {
        val initial: IndividualChatState = IndividualChatState(
            loading = false,
            error = UiError(false, null)
//            counter = 0,
        )
    }
}

sealed class IndividualChatIntent: IndividualChatntent {
    data object Loading: IndividualChatIntent()
    data class Error(val errorMsg: String): IndividualChatIntent()
    data object CloseError: IndividualChatIntent()
}