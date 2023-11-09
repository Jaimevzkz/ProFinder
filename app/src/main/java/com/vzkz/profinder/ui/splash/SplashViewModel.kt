package com.vzkz.profinder.ui.splash

import com.vzkz.profinder.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(): BaseViewModel<SplashState, SplashIntent>(SplashState.initial) {

    override fun reduce(state: SplashState, intent: SplashIntent): SplashState { //This function reduces each intent with a when
        TODO("Not yet implemented")
    }

    //Observe events from UI and dispatch them
    fun onX(){ //Example fun
        dispatch(SplashIntent.Loading)
    }


}