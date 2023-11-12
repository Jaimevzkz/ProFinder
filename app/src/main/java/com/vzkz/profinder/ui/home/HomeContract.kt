package com.vzkz.profinder.ui.home

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State


data class HomeState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = false,
//            counter = 0,
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class HomeIntent: Intent {
    data class Loading(val isLoading: Boolean): HomeIntent()
}