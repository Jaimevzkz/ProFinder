package com.vzkz.profinder.ui.services

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.jobs.AddJobOrRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.CheckExistingRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.location.GetLocationUseCase
import com.vzkz.profinder.domain.usecases.services.ChangeServiceActivityUseCase
import com.vzkz.profinder.domain.usecases.services.DeleteServiceUseCase
import com.vzkz.profinder.domain.usecases.services.GetActiveServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.InsertServiceUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
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
    private val getLocationUseCase: GetLocationUseCase
) : BaseViewModel<ServicesState, ServicesIntent>(ServicesState.initial) {

    override fun reduce(state: ServicesState, intent: ServicesIntent): ServicesState {
        return when (intent) {
            is ServicesIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = true
            )

            is ServicesIntent.SetServiceLists -> state.copy(
                error = UiError(false, null),
                loading = false,
                activeServiceList = intent.lists.first,
                inActiveServiceList = intent.lists.second
            )

            is ServicesIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )

            is ServicesIntent.SetUser -> state.copy(
                user = intent.user,
                error = UiError(false, null),
                loading = false
            )

            is ServicesIntent.SetActiveServiceLists -> state.copy(
                error = UiError(false, null),
                loading = false,
                activeServiceList = intent.list
            )

            is ServicesIntent.SetRequestExists -> state.copy(requestExists = intent.requestExists)
            is ServicesIntent.SetLocation -> state.copy(location = intent.location)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val user = getUserUseCase()
                dispatch(ServicesIntent.SetUser(user))
                when (user.actor) {
                    Actors.User -> dispatch(
                        ServicesIntent.SetActiveServiceLists(
                            getActiveServiceListUseCase()
                        )
                    )

                    Actors.Professional -> {
                        val serviceLists = getServiceListUseCase()
                        dispatch(ServicesIntent.SetServiceLists(serviceLists))
                    }
                }
            }
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message.orEmpty()))
        }
    }

    fun onAddService(service: ServiceModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                insertServiceUseCase(service)
                onInit()
            }
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message.orEmpty()))
        }
    }

    fun onDeleteService(sid: String) {
        try {
            deleteServiceUseCase(sid)
            onInit()
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message.orEmpty()))
        }
    }

    fun onChangeActivity(service: ServiceModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                changeServiceActivityUseCase(service.sid, service.isActive)
                onInit()
            }
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message.orEmpty()))
        }
    }

    fun onSetProfileToSee(actor: ActorModel) = userProfileToSeeUseCase.setUser(actor)

    fun onCloseDialog() = dispatch(ServicesIntent.CloseError)

    fun onRequestService(service: ServiceModel) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                addRequestsUseCase(isRequest = true, serviceModel = service)
                dispatch(ServicesIntent.SetRequestExists(ServiceState.REQUESTED))
            }
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message.orEmpty()))
        }
    }

    fun checkExistingRequests(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(ServicesIntent.SetRequestExists(checkExistingRequestUseCase(sid = sid)))
        }
    }

    fun onDeleteRequest(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRequestUseCase(isRequest = true, sid = sid)
            dispatch(ServicesIntent.SetRequestExists(ServiceState.FREE))
        }
    }

    fun onGetLocation(){
        viewModelScope.launch(Dispatchers.IO) {
            getLocationUseCase().collect { location ->
                dispatch(ServicesIntent.SetLocation(location))
            }
        }
    }


}