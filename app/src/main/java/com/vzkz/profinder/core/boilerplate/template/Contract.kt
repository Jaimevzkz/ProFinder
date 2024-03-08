package com.vzkz.profinder.core.boilerplate.template

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
    data object Loading: tIntent()
    data class Error(val errorMsg: String): tIntent()
    data object CloseError: tIntent()
}