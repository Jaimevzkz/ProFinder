package com.vzkz.profinder.ui.home

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.GetRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.RateJobUseCase
import com.vzkz.profinder.domain.usecases.jobs.TurnJobIntoRequestUseCase
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.ui.asUiText
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
    private val turnJobIntoRequestUseCase: TurnJobIntoRequestUseCase,
    private val rateJobUseCase: RateJobUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(state: HomeState, intent: HomeIntent): HomeState {
        return when (intent) {
            is HomeIntent.Loading -> state.copy(
                loading = !state.loading
            )

            is HomeIntent.Error -> state.copy(
                error = intent.error,
                loading = false
            )

            HomeIntent.CloseError -> state.copy(
                error = null,
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

            is HomeIntent.ChangeJobList -> state.copy(
                jobList = intent.jobList,
                loading = !state.loading
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        setJobs()
        setRequests()
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(HomeIntent.SetIsUser(getUserUseCase().actor == Actors.User))
            dispatch(HomeIntent.ChangeFavList(favouriteListUseCase.getFavouriteList()))
        }
    }

    private fun setRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val requests = getRequestsUseCase(isRequest = true)) {
                is Result.Success -> {
                    requests.data.collect { requestList ->
                        dispatch(HomeIntent.ChangeRequestList(requestList))
                        dispatch(HomeIntent.Loading)
                    }
                }

                is Result.Error -> dispatch(HomeIntent.Error(requests.error.asUiText()))
            }
        }
    }

    private fun setJobs() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val jobs = getRequestsUseCase(isRequest = false)) {
                is Result.Success -> {
                    jobs.data.collect { jobList ->
                        dispatch(HomeIntent.ChangeJobList(jobList))
                        dispatch(HomeIntent.Loading)
                    }
                }

                is Result.Error -> dispatch(HomeIntent.Error(jobs.error.asUiText()))
            }

        }
    }

    fun onDeleteFav(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val favList =
                favouriteListUseCase.changeFavouriteList(uidToChange = uid, add = false)) {
                is Result.Success -> dispatch(HomeIntent.ChangeFavList(favouriteListUseCase.getFavouriteList()))
                is Result.Error -> dispatch(HomeIntent.Error(favList.error.asUiText()))
            }
        }
    }

    fun onCloseDialog() = dispatch(HomeIntent.CloseError)

    fun onDeleteRequest(rid: String, otherUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteRequestUseCase.deleteWithRid(isRequest = true, id = rid, otherUid = otherUid)
        }
    }

    fun onAcceptRequest(request: JobModel) {
        viewModelScope.launch(Dispatchers.IO) {
            turnJobIntoRequestUseCase(request)
        }
    }

    fun onRateJob(job: JobModel, rating: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            rateJobUseCase(job = job, rating = rating)
        }
    }
}