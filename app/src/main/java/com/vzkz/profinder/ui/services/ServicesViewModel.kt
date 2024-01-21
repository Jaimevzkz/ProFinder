package com.vzkz.profinder.ui.services

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.usecases.services.ChangeServiceActivityUseCase
import com.vzkz.profinder.domain.usecases.services.DeleteServiceUseCase
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.InsertServiceUseCase
import com.vzkz.profinder.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServiceListUseCase: GetServiceListUseCase,
    private val insertServiceUseCase: InsertServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val changeServiceActivityUseCase: ChangeServiceActivityUseCase,
): BaseViewModel<ServicesState, ServicesIntent>(ServicesState.initial) {

    override fun reduce(state: ServicesState, intent: ServicesIntent): ServicesState {
        return when(intent){
            is ServicesIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = true
            )
            is ServicesIntent.SetServiceLists -> state.copy(
                error = Error(false, null),
                loading = false,
                activeServiceList = intent.lists.first,
                inActiveServiceList = intent.lists.second
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val serviceLists = getServiceListUseCase()
                dispatch(ServicesIntent.SetServiceLists(serviceLists))
            }
        } catch (e: Exception) {
            dispatch(ServicesIntent.Error(e.message ?: "Error getting  service lists"))
        }
    }

    fun onAddService(service: ServiceModel){
        viewModelScope.launch(Dispatchers.IO) {
            insertServiceUseCase(service)
            onInit()
        }
    }

    fun onDeleteService(sid: String){
        deleteServiceUseCase(sid)
        onInit()
    }

    fun onChangeActivity(service: ServiceModel){
        viewModelScope.launch(Dispatchers.IO) {
            changeServiceActivityUseCase(service.sid, service.isActive)
            onInit()
        }
    }


}