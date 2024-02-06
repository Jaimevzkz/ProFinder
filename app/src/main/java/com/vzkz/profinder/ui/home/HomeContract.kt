package com.vzkz.profinder.ui.home

import com.vzkz.profinder.core.boilerplate.Intent
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

sealed class tIntent: Intent {
    data class Loading(val isLoading: Boolean): tIntent()
    data class Error(val errorMsg: String): tIntent()
    data object CloseError: tIntent()
}