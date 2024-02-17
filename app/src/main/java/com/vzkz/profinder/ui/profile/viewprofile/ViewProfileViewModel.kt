package com.vzkz.profinder.ui.profile.viewprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    private val userProfileToSeeUseCase: UserProfileToSeeUseCase
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
                user = intent.user,
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit(uid: String){ //Example fun
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(ViewProfileIntent.Loading)
            val user = userProfileToSeeUseCase.getUser(uid)
            dispatch(ViewProfileIntent.Setuser(user))
        }
    }

    fun onCloseDialog() = dispatch(ViewProfileIntent.CloseError)

}