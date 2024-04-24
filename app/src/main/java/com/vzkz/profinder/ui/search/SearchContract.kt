package com.vzkz.profinder.ui.search

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.UiText


data class SearchState(
    val loading: Boolean,
    val error: UiText?,
    val userList: List<ActorModel>
) : State {
    companion object {
        val initial: SearchState = SearchState(
            loading = true,
            error = null,
            userList = emptyList()
        )
    }
}

sealed class SearchIntent: Intent {
    data object Loading: SearchIntent()
    data class Error(val error: UiText): SearchIntent()
    data class SetUserList(val userList: List<ActorModel>): SearchIntent()
    data object CloseError: SearchIntent()
}