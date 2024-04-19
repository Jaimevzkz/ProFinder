package com.vzkz.profinder.core.boilerplate.template

import com.vzkz.profinder.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class tViewModel @Inject constructor(): BaseViewModel<tState, tIntent>(tState.initial) {

    override fun reduce(state: tState, intent: tIntent): tState {
        return when(intent){
            is tIntent.Loading -> state.copy(loading = true)


            is tIntent.Error -> state.copy(
                error = intent.error,
                loading = false
            )

            tIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onCloseDialog() = dispatch(tIntent.CloseError)

}