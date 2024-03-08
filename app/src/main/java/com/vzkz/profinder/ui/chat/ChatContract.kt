package com.vzkz.profinder.ui.chat

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.UiError


data class tState(
    val loading: Boolean,
    val error: UiError
//    val counter: Int,
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
            error = UiError(false, null)
//            counter = 0,
        )
    }
}

sealed class tIntent: IndividualChatntent {
    data class Loading(val isLoading: Boolean): tIntent()
    data class Error(val errorMsg: String): tIntent()
    data object CloseError: tIntent()
}