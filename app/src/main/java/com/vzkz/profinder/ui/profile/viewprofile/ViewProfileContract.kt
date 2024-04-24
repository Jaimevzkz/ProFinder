package com.vzkz.profinder.ui.profile.viewprofile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.ui.UiText
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState


data class ViewProfileState(
    val loading: Boolean,
    val error: UiText?,
    val userToSee: ActorModel?,
    val isFavourite: Boolean,
    val uid: String?,
    val serviceList: List<ServiceModel>,
    val requestExists: ServiceState
) : State {
    companion object {
        val initial: ViewProfileState = ViewProfileState(
            loading = true,
            error = null,
            userToSee = null,
            isFavourite = false,
            uid = null,
            serviceList = emptyList(),
            requestExists = ServiceState.FREE
        )
    }
}

sealed class ViewProfileIntent : Intent {
    data object Loading : ViewProfileIntent()
    data class Error(val error: UiText) : ViewProfileIntent()
    data object CloseError : ViewProfileIntent()
    data class SetUser(val user: ActorModel) : ViewProfileIntent()
    data class ChangeFavourite(val newValue: Boolean) : ViewProfileIntent()
    data class SetUid(val uid: String) : ViewProfileIntent()
    data class SetServiceList(val servileList: List<ServiceModel>) : ViewProfileIntent()
    data class SetRequestExists(val requestExists: ServiceState) : ViewProfileIntent()

}