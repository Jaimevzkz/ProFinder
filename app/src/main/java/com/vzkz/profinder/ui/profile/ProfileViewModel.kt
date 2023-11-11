package com.vzkz.profinder.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.usecases.LogoutUseCase
import com.vzkz.profinder.ui.login.LoginIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(private val logoutUseCase: LogoutUseCase): BaseViewModel<ProfileState, ProfileIntent>(ProfileState.initial) {

    override fun reduce(state: ProfileState, intent: ProfileIntent): ProfileState { //This function reduces each intent with a when
        return when(intent){
            ProfileIntent.Logout -> state.copy(logout = true)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onLogout(){ //Example fun
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
        }
        dispatch(ProfileIntent.Logout)
    }


}