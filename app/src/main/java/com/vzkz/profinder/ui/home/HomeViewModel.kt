package com.vzkz.profinder.ui.home

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.GetRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.TurnJobIntoRequestUseCase
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favouriteListUseCase: FavouriteListUseCase,
    private val getRequestsUseCase: GetRequestsUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val deleteRequestUseCase: DeleteJobOrRequestUseCase,
    private val turnJobIntoRequestUseCase: TurnJobIntoRequestUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(state: HomeState, intent: HomeIntent): HomeState {
        return when (intent) {
            is HomeIntent.Loading -> state.copy(
                loading = !state.loading
            )

            is HomeIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            HomeIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )

            is HomeIntent.ChangeFavList -> state.copy(
                favList = intent.favList,
                loading = false
            )

            is HomeIntent.ChangeRequestList -> state.copy(
                requestList = intent.requestList,
                loading = !state.loading
            )

            is HomeIntent.SetIsUser -> state.copy(isUser = intent.isUser)

            is HomeIntent.ChangeJobList -> state.copy(jobList = intent.jobList)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            setJobs()
            setRequests()
            viewModelScope.launch(Dispatchers.IO) {
                dispatch(HomeIntent.SetIsUser(getUserUseCase().actor == Actors.User))
                dispatch(HomeIntent.ChangeFavList(favouriteListUseCase.getFavouriteList()))
            }
        } catch (e: Exception) {
            dispatch(HomeIntent.Error(e.message.orEmpty()))
        }
    }

    private fun setRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            getRequestsUseCase(isRequest = true).collect { requestList ->
                dispatch(HomeIntent.ChangeRequestList(requestList))
                dispatch(HomeIntent.Loading)
            }
        }
    }
    private fun setJobs() {
        viewModelScope.launch(Dispatchers.IO) {
            getRequestsUseCase(isRequest = false).collect { jobList ->
                dispatch(HomeIntent.ChangeJobList(jobList))
//                dispatch(HomeIntent.Loading)
            }
        }
    }

    fun onDeleteFav(uid: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                favouriteListUseCase.changeFavouriteList(uidToChange = uid, add = false)
                dispatch(HomeIntent.ChangeFavList(favouriteListUseCase.getFavouriteList()))
            }
        } catch (e: Exception) {
            dispatch(HomeIntent.Error(e.message.orEmpty()))
        }
    }

    fun onCloseDialog() = dispatch(HomeIntent.CloseError)

    fun onDeleteRequest(rid: String, otherUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRequestUseCase.deleteWithRid(isRequest = true, id = rid, otherUid = otherUid)
        }
    }

    fun onAcceptRequest(request: JobModel){
        viewModelScope.launch(Dispatchers.IO) {
            turnJobIntoRequestUseCase(request)
        }
    }
}