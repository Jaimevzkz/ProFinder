package com.vzkz.profinder.core.boilerplate.template

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.ui.UiText


data class tState(
    val loading: Boolean,
    val error: UiText?
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
            error = null
        )
    }
}

sealed class tIntent: Intent {
    data object Loading: tIntent()
    data class Error(val error: UiText): tIntent()
    data object CloseError: tIntent()
}