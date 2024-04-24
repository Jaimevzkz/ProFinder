package com.vzkz.profinder.ui.home

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.ui.UiText


data class HomeState(
    val loading: Boolean,
    val isUser: Boolean,
    val error: UiText?,
    val favList: List<ActorModel>,
    val requestList: List<JobModel>,
    val jobList: List<JobModel>
) : State {
    companion object {
        val initial: HomeState = HomeState(
            loading = true,
            isUser = false,
            error = null,
            favList = emptyList(),
            requestList = emptyList(),
            jobList = emptyList()
        )
    }
}

sealed class HomeIntent: Intent {
    data object Loading: HomeIntent()
    data class Error(val error: UiText): HomeIntent()
    data object CloseError: HomeIntent()
    data class ChangeFavList(val favList: List<ActorModel>): HomeIntent()
    data class ChangeRequestList(val requestList: List<JobModel>): HomeIntent()
    data class ChangeJobList(val jobList: List<JobModel>): HomeIntent()
    data class SetIsUser(val isUser: Boolean): HomeIntent()
}