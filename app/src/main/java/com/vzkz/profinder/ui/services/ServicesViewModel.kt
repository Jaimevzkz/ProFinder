package com.vzkz.profinder.ui.services

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.usecases.jobs.AddJobOrRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.CheckExistingRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.location.GetLocationUseCase
import com.vzkz.profinder.domain.usecases.location.GetOtherLocationsUseCase
import com.vzkz.profinder.domain.usecases.services.ChangeServiceActivityUseCase
import com.vzkz.profinder.domain.usecases.services.DeleteServiceUseCase
import com.vzkz.profinder.domain.usecases.services.GetActiveServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.InsertServiceUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import com.vzkz.profinder.ui.asUiText
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServiceListUseCase: GetServiceListUseCase,
    private val getActiveServiceListUseCase: GetActiveServiceListUseCase,
    private val insertServiceUseCase: InsertServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val changeServiceActivityUseCase: ChangeServiceActivityUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val userProfileToSeeUseCase: UserProfileToSeeUseCase,
    private val addRequestsUseCase: AddJobOrRequestsUseCase,
    private val checkExistingRequestUseCase: CheckExistingRequestUseCase,
    private val deleteRequestUseCase: DeleteJobOrRequestUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getLocationsUseCase: GetOtherLocationsUseCase
) : BaseViewModel<ServicesState, ServicesIntent>(ServicesState.initial) {

    override fun reduce(state: ServicesState, intent: ServicesIntent): ServicesState {
        return when (intent) {
            is ServicesIntent.Error -> state.copy(
                error = intent.error,
                loading = true
            )

            is ServicesIntent.SetServiceLists -> state.copy(
                loading = false,
                activeServiceList = intent.lists.first,
                inActiveServiceList = intent.lists.second
            )

            is ServicesIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )

            is ServicesIntent.SetUser -> state.copy(
                user = intent.user,
                loading = false
            )

            is ServicesIntent.SetActiveServiceLists -> state.copy(
                loading = false,
                activeServiceList = intent.list
            )

            is ServicesIntent.SetRequestExists -> state.copy(requestExists = intent.requestExists)
            is ServicesIntent.SetLocation -> state.copy(location = intent.location)
            is ServicesIntent.SetOtherLocations -> state.copy(otherLocations = intent.locationList)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        onGetLocation()
        onGetOtherLocations()
        onUpdateFirestoreLocation()
        viewModelScope.launch(Dispatchers.IO) {
            when (val user = getUserUseCase()) {
                is Result.Success -> {
                    dispatch(ServicesIntent.SetUser(user.data))
                    when (user.data.actor) {
                        Actors.User -> {
                            when (val activeServiceList = getActiveServiceListUseCase()) {
                                is Result.Success -> dispatch(
                                    ServicesIntent.SetActiveServiceLists(
                                        activeServiceList.data
                                    )
                                )

                                is Result.Error -> dispatch(ServicesIntent.Error(activeServiceList.error.asUiText()))
                            }

                        }

                        Actors.Professional -> {
                            when (val serviceList = getServiceListUseCase()) {
                                is Result.Success -> dispatch(
                                    ServicesIntent.SetServiceLists(
                                        serviceList.data
                                    )
                                )

                                is Result.Error -> dispatch(ServicesIntent.Error(serviceList.error.asUiText()))
                            }
                        }
                    }
                }

                is Result.Error -> dispatch(ServicesIntent.Error(user.error.asUiText()))
            }

        }
    }

    fun onAddService(service: ServiceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val insertion = insertServiceUseCase(service)) {
                is Result.Success -> onInit()
                is Result.Error -> dispatch(ServicesIntent.Error(insertion.error.asUiText()))
            }
        }
    }

    fun onDeleteService(sid: String) {
        when (val deletion = deleteServiceUseCase(sid)) {
            is Result.Success -> onInit()
            is Result.Error -> dispatch(ServicesIntent.Error(deletion.error.asUiText()))
        }
    }

    fun onChangeActivity(service: ServiceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val change = changeServiceActivityUseCase(service.sid, service.isActive)) {
                is Result.Success -> onInit()
                is Result.Error -> dispatch(ServicesIntent.Error(change.error.asUiText()))
            }
        }
    }

    fun onSetProfileToSee(actor: ActorModel) = userProfileToSeeUseCase.setUser(actor)

    fun onCloseDialog() = dispatch(ServicesIntent.CloseError)

    fun onRequestService(service: ServiceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val request = addRequestsUseCase(isRequest = true, serviceModel = service)) {
                is Result.Success -> dispatch(ServicesIntent.SetRequestExists(ServiceState.REQUESTED))
                is Result.Error -> dispatch(ServicesIntent.Error(request.error.asUiText()))
            }
        }
    }

    fun checkExistingRequests(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val check = checkExistingRequestUseCase(sid = sid)) {
                is Result.Success -> dispatch(ServicesIntent.SetRequestExists(check.data))
                is Result.Error -> dispatch(ServicesIntent.Error(check.error.asUiText()))
            }
        }
    }

    fun onDeleteRequest(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val deletion = deleteRequestUseCase(isRequest = true, sid = sid)) {
                is Result.Success -> dispatch(ServicesIntent.SetRequestExists(ServiceState.FREE))
                is Result.Error -> dispatch(ServicesIntent.Error(deletion.error.asUiText()))
            }
        }
    }

    private fun onGetLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            getLocationUseCase().collect { location ->
                dispatch(ServicesIntent.SetLocation(location))
            }
        }
    }

    private fun onGetOtherLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val locations = getLocationsUseCase()){
                is Result.Success -> {
                    locations.data.collect{locationList ->
                        dispatch(ServicesIntent.SetOtherLocations(locationList))
                    }
                }
                is Result.Error -> dispatch(ServicesIntent.Error(locations.error.asUiText()))
            }
        }
    }

    private fun onUpdateFirestoreLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val update = getLocationUseCase.updateFirestoreLocation()) {
                is Result.Success -> {/*do nothing*/}

                is Result.Error -> dispatch(ServicesIntent.Error(update.error.asUiText()))
            }
        }
    }


}