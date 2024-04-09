package com.vzkz.profinder.ui.services

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val user: ActorModel?,
    val requestExists: Boolean,
    val error: UiError,
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
            user = null,
            requestExists = false,
            activeServiceList = emptyList(),
            inActiveServiceList = emptyList(),
            error = UiError(isError = false, errorMsg = null)
        )
    }
}

sealed class ServicesIntent : IndividualChatntent {
    data class SetServiceLists(val lists: Pair<List<ServiceModel>, List<ServiceModel>>) : ServicesIntent()
    data class SetActiveServiceLists(val list: List<ServiceModel>) : ServicesIntent()
    data class Error(val errorMsg: String) : ServicesIntent()
    data object CloseError: ServicesIntent()
    data class SetUser(val user: ActorModel?) : ServicesIntent()
    data class SetRequestExists(val requestExists: Boolean) : ServicesIntent()
}