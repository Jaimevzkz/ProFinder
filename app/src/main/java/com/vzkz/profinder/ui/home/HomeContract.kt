package com.vzkz.profinder.ui.home

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.RequestModel
import com.vzkz.profinder.domain.model.UiError


data class HomeState(
    val loading: Boolean,
    val isUser: Boolean,
    val error: UiError,
    val favList: List<ActorModel>,
    val requestList: List<RequestModel>
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = true,
            isUser = false,
            error = UiError(false, null),
            favList = emptyList(),
            requestList = emptyList()
        )
    }
}

sealed class HomeIntent: IndividualChatntent {
    data object Loading: HomeIntent()
    data class Error(val errorMsg: String): HomeIntent()
    data object CloseError: HomeIntent()
    data class ChangeFavList(val favList: List<ActorModel>): HomeIntent()
    data class ChangeRequestList(val requestList: List<RequestModel>): HomeIntent()
    data class SetIsUser(val isUser: Boolean): HomeIntent()
}