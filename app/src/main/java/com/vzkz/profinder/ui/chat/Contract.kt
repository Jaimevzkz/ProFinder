package com.vzkz.profinder.ui.chat

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.ui.signup.SignUpIntent


data class tState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: tState = tState(
            loading = false,
//            counter = 0,
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class tIntent: Intent {
    data class Loading(val isLoading: Boolean): tIntent()
}