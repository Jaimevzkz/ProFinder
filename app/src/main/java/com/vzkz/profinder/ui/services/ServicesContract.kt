package com.vzkz.profinder.ui.services

import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val user: ActorModel?,
    val requestExists: ServiceState,
    val error: UiError,
    val location: LatLng?
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
            user = null,
            requestExists = ServiceState.FREE,
            activeServiceList = emptyList(),
            inActiveServiceList = emptyList(),
            error = UiError(isError = false, errorMsg = null),
            location = null
        )
    }
}

sealed class ServicesIntent : IndividualChatntent {
    data class SetServiceLists(val lists: Pair<List<ServiceModel>, List<ServiceModel>>) : ServicesIntent()
    data class SetActiveServiceLists(val list: List<ServiceModel>) : ServicesIntent()
    data class Error(val errorMsg: String) : ServicesIntent()
    data object CloseError: ServicesIntent()
    data class SetUser(val user: ActorModel?) : ServicesIntent()
    data class SetRequestExists(val requestExists: ServiceState) : ServicesIntent()
    data class SetLocation(val location: LatLng?) : ServicesIntent()
}