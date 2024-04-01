package com.vzkz.profinder.ui.profile.viewprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    private val userProfileToSeeUseCase: UserProfileToSeeUseCase,
    private val favouriteListUseCase: FavouriteListUseCase
): BaseViewModel<ViewProfileState, ViewProfileIntent>(ViewProfileState.initial) {

    override fun reduce(state: ViewProfileState, intent: ViewProfileIntent): ViewProfileState {
        return when(intent){
            is ViewProfileIntent.Loading -> state.copy(loading = true)


            is ViewProfileIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            ViewProfileIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )

            is ViewProfileIntent.Setuser -> state.copy(
                userToSee = intent.user,
                loading = false
            )

            is ViewProfileIntent.ChangeFavourite -> state.copy(
                isFavourite = intent.newValue
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit(uidToSee: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userToSee = userProfileToSeeUseCase.getUser(uidToSee)
            val isFavourite = favouriteListUseCase.checkIsFavourite(uidToSee)
            dispatch(ViewProfileIntent.ChangeFavourite(isFavourite))
            dispatch(ViewProfileIntent.Setuser(userToSee))
        }
    }

    fun onCloseDialog() = dispatch(ViewProfileIntent.CloseError)

    fun onFavouriteChanged(uidToChange: String, add: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteListUseCase.changeFavouriteList(uidToChange, add)
            dispatch(ViewProfileIntent.ChangeFavourite(add))
        }
    }

}