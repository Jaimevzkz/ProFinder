package com.vzkz.profinder.ui.home

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError


data class HomeState(
    val loading: Boolean,
    val error: UiError,
    val favList: List<ActorModel>
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = true,
            error = UiError(false, null),
            favList = emptyList()
        )
    }
}

sealed class HomeIntent: Intent {
    data object Loading: HomeIntent()
    data class Error(val errorMsg: String): HomeIntent()
    data object CloseError: HomeIntent()
    data class ChangeFavList(val favList: List<ActorModel>): HomeIntent()
}