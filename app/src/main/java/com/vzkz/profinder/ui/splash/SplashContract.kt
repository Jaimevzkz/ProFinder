package com.vzkz.profinder.ui.splash

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State


data class SplashState(
    val loading: Boolean,
//    val counter: Int,
) : State {
    companion object {
        val initial: SplashState = SplashState(
            loading = false,
//            counter = 0,
        )
    }
}

sealed class SplashIntent: Intent {
    data object Loading: SplashIntent()
}