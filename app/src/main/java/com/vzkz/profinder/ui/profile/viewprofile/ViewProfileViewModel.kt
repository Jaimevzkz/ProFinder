package com.vzkz.profinder.ui.profile.viewprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.usecases.jobs.AddJobOrRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.CheckExistingRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCase
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import com.vzkz.profinder.ui.asUiText
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    private val userProfileToSeeUseCase: UserProfileToSeeUseCase,
    private val favouriteListUseCase: FavouriteListUseCase,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val uidCombiner: UidCombiner,
    private val checkExistingRequestUseCase: CheckExistingRequestUseCase,
    private val addRequestsUseCase: AddJobOrRequestsUseCase,
    private val deleteRequestUseCase: DeleteJobOrRequestUseCase,
    private val getServiceListUseCase: GetServiceListUseCase
) : BaseViewModel<ViewProfileState, ViewProfileIntent>(ViewProfileState.initial) {

    override fun reduce(state: ViewProfileState, intent: ViewProfileIntent): ViewProfileState {
        return when (intent) {
            is ViewProfileIntent.Loading -> state.copy(loading = true)


            is ViewProfileIntent.Error -> state.copy(
                error = intent.error,
                loading = false
            )

            ViewProfileIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )

            is ViewProfileIntent.SetUser -> state.copy(
                userToSee = intent.user,
                loading = false
            )

            is ViewProfileIntent.ChangeFavourite -> state.copy(
                isFavourite = intent.newValue
            )

            is ViewProfileIntent.SetUid -> state.copy(uid = intent.uid)

            is ViewProfileIntent.SetServiceList -> state.copy(serviceList = intent.servileList)

            is ViewProfileIntent.SetRequestExists -> state.copy(requestExists = intent.requestExists)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit(uidToSee: String) {
        onGetServiceList(uidToSee)
        viewModelScope.launch(Dispatchers.IO) {
            when (val userToSee = userProfileToSeeUseCase.getUser(uidToSee)) {
                is Result.Success -> {
                    val isFavourite = favouriteListUseCase.checkIsFavourite(uidToSee)
                    dispatch(ViewProfileIntent.ChangeFavourite(isFavourite))
                    dispatch(ViewProfileIntent.SetUid(getUidDataStoreUseCase()))
                    dispatch(ViewProfileIntent.SetUser(userToSee.data))
                }

                is Result.Error -> dispatch(ViewProfileIntent.Error(userToSee.error.asUiText()))
            }

        }
    }

    fun onCloseDialog() = dispatch(ViewProfileIntent.CloseError)

    fun onFavouriteChanged(uidToChange: String, add: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val favList = favouriteListUseCase.changeFavouriteList(uidToChange, add)) {
                is Result.Success -> dispatch(ViewProfileIntent.ChangeFavourite(add))
                is Result.Error -> dispatch(ViewProfileIntent.Error(favList.error.asUiText()))
            }
        }
    }

    fun combineUids(uid1: String, uid2: String) = uidCombiner.combineUids(uid1 = uid1, uid2 = uid2)

    private fun onGetServiceList(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val serviceList = getServiceListUseCase.getOtherServiceList(uid)) {
                is Result.Success -> dispatch(ViewProfileIntent.SetServiceList(serviceList.data))
                is Result.Error -> dispatch(ViewProfileIntent.Error(serviceList.error.asUiText()))
            }
        }
    }

    fun checkExistingRequests(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val check = checkExistingRequestUseCase(sid = sid)) {
                is Result.Success -> dispatch(ViewProfileIntent.SetRequestExists(check.data))
                is Result.Error -> dispatch(ViewProfileIntent.Error(check.error.asUiText()))
            }
        }
    }

    fun onRequestService(service: ServiceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val request = addRequestsUseCase(isRequest = true, serviceModel = service)) {
                is Result.Success -> dispatch(ViewProfileIntent.SetRequestExists(ServiceState.REQUESTED))
                is Result.Error -> dispatch(ViewProfileIntent.Error(request.error.asUiText()))
            }
        }
    }

    fun onDeleteRequest(sid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val deletion = deleteRequestUseCase(isRequest = true, sid = sid)) {
                is Result.Success -> dispatch(ViewProfileIntent.SetRequestExists(ServiceState.FREE))
                is Result.Error -> dispatch(ViewProfileIntent.Error(deletion.error.asUiText()))
            }
        }

    }
}