package com.vzkz.profinder.ui.services

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val error: UiError,
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
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
}