package com.vzkz.profinder.ui.chat

import com.vzkz.profinder.core.DateFormatter
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dateFormatter: DateFormatter
): BaseViewModel<tState, tIntent>(tState.initial) {

    override fun reduce(state: tState, intent: tIntent): tState {
        return when(intent){
            is tIntent.Loading -> TODO()


            is tIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            tIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun onCloseDialog() = dispatch(tIntent.CloseError)

    fun getFormattedTime(timestamp: Long): String {
        return dateFormatter.formatToTime(timestamp)
    }
}