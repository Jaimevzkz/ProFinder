package com.vzkz.profinder.ui.profile.viewprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import com.vzkz.profinder.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    private val userProfileToSeeUseCase: UserProfileToSeeUseCase,
    private val favouriteListUseCase: FavouriteListUseCase,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val uidCombiner: UidCombiner
): BaseViewModel<ViewProfileState, ViewProfileIntent>(ViewProfileState.initial) {

    override fun reduce(state: ViewProfileState, intent: ViewProfileIntent): ViewProfileState {
        return when(intent){
            is ViewProfileIntent.Loading -> state.copy(loading = true)


            is ViewProfileIntent.Error -> state.copy(
                error = intent.error,
                loading = false
            )

            ViewProfileIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )

            is ViewProfileIntent.Setuser -> state.copy(
                userToSee = intent.user,
                loading = false
            )

            is ViewProfileIntent.ChangeFavourite -> state.copy(
                isFavourite = intent.newValue
            )

            is ViewProfileIntent.SetUid -> state.copy(uid = intent.uid)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit(uidToSee: String){
        viewModelScope.launch(Dispatchers.IO) {
            val userToSee = userProfileToSeeUseCase.getUser(uidToSee)
            val isFavourite = favouriteListUseCase.checkIsFavourite(uidToSee)
            dispatch(ViewProfileIntent.ChangeFavourite(isFavourite))
            dispatch(ViewProfileIntent.SetUid(getUidDataStoreUseCase()))
            dispatch(ViewProfileIntent.Setuser(userToSee))
        }
    }

    fun onCloseDialog() = dispatch(ViewProfileIntent.CloseError)

    fun onFavouriteChanged(uidToChange: String, add: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val favList = favouriteListUseCase.changeFavouriteList(uidToChange, add)){
                is Result.Success -> dispatch(ViewProfileIntent.ChangeFavourite(add))
                is Result.Error -> dispatch(ViewProfileIntent.Error(favList.error.asUiText()))
            }
        }
    }

    fun combineUids(uid1: String, uid2: String) = uidCombiner.combineUids(uid1 = uid1, uid2 = uid2)
}