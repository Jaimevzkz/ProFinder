package com.vzkz.profinder.ui.services

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.profile.Error


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val error: Error,
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
            activeServiceList = emptyList(),
            inActiveServiceList = emptyList(),
            error = Error(isError = false, errorMsg = null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class ServicesIntent : Intent {
    data class SetServiceLists(val lists: Pair<List<ServiceModel>, List<ServiceModel>>) : ServicesIntent()
    data class Error(val errorMsg: String) : ServicesIntent()
//    data class AddService(val service: ServiceModel) : ServicesIntent()
//    data class DeleteService(val sid: String, val isActive: Boolean) : ServicesIntent()
}