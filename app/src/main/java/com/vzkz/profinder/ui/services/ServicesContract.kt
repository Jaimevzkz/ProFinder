package com.vzkz.profinder.ui.services

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.profile.ProfileIntent


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val user: ActorModel?,
    val error: UiError,
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
            user = null,
            activeServiceList = emptyList(),
            inActiveServiceList = emptyList(),
            error = UiError(isError = false, errorMsg = null)
        )
    }
}

sealed class ServicesIntent : Intent {
    data class SetServiceLists(val lists: Pair<List<ServiceModel>, List<ServiceModel>>) : ServicesIntent()
    data class Error(val errorMsg: String) : ServicesIntent()
    data object CloseError: ServicesIntent()
    data class SetUser(val user: ActorModel?) : ServicesIntent()
}