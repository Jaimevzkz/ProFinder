package com.vzkz.profinder.ui.home

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.ui.services.ServicesIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val favouriteListUseCase: FavouriteListUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(state: HomeState, intent: HomeIntent): HomeState {
        return when (intent) {
            is HomeIntent.Loading -> TODO()


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
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                dispatch(HomeIntent.ChangeFavList(favouriteListUseCase.getFavouriteList()))
            }
        } catch (e: Exception) {
            dispatch(HomeIntent.Error(e.message.orEmpty()))
        }
    }

    fun onCloseDialog() = dispatch(HomeIntent.CloseError)

}