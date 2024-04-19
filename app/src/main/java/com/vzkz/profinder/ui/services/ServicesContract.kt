package com.vzkz.profinder.ui.services

import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.LocationModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState


data class ServicesState(
    val loading: Boolean,
    val activeServiceList: List<ServiceModel>,
    val inActiveServiceList: List<ServiceModel>,
    val user: ActorModel?,
    val requestExists: ServiceState,
    val error: UiText?,
    val location: LatLng?,
    val otherLocations: List<LocationModel>
) : State {
    companion object {
        val initial: ServicesState = ServicesState(
            loading = true,
            user = null,
            requestExists = ServiceState.FREE,
            activeServiceList = emptyList(),
            inActiveServiceList = emptyList(),
            error = null,
            location = null,
            otherLocations = emptyList()
        )
    }
}

sealed class ServicesIntent : IndividualChatntent {
    data class SetServiceLists(val lists: Pair<List<ServiceModel>, List<ServiceModel>>) : ServicesIntent()
    data class SetActiveServiceLists(val list: List<ServiceModel>) : ServicesIntent()
    data class Error(val error: UiText) : ServicesIntent()
    data object CloseError: ServicesIntent()
    data class SetUser(val user: ActorModel?) : ServicesIntent()
    data class SetRequestExists(val requestExists: ServiceState) : ServicesIntent()
    data class SetLocation(val location: LatLng?) : ServicesIntent()
    data class SetOtherLocations(val locationList: List<LocationModel>) : ServicesIntent()
}