package com.vzkz.profinder.ui.chat.individualchat

import com.vzkz.profinder.core.DateFormatter
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class IndividualChatViewModel @Inject constructor(
    private val dateFormatter: DateFormatter
) :
    BaseViewModel<IndividualChatState, IndividualChatIntent>(IndividualChatState.initial) {

    override fun reduce(
        state: IndividualChatState,
        intent: IndividualChatIntent
    ): IndividualChatState {
        return when (intent) {
            is IndividualChatIntent.Loading -> state.copy(loading = true)


            is IndividualChatIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            IndividualChatIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun getFormattedTime(timestamp: Long): String {
        return dateFormatter.formatToTime(timestamp)
    }

    fun onCloseDialog() = dispatch(IndividualChatIntent.CloseError)

}