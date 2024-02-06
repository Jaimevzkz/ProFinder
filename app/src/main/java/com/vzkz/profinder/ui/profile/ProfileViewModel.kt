package com.vzkz.profinder.ui.profile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.LogoutUseCase
import com.vzkz.profinder.domain.usecases.user.ChangeStateUseCase
import com.vzkz.profinder.domain.usecases.user.ChangeStateUseCaseImpl
import com.vzkz.profinder.ui.login.LoginIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val changeStateUseCase: ChangeStateUseCase
) : BaseViewModel<ProfileState, ProfileIntent>(ProfileState.initial) {
    override fun reduce(
        state: ProfileState,
        intent: ProfileIntent
    ): ProfileState { //This function reduces each intent with a when
        return when (intent) {
            ProfileIntent.Logout -> state.copy(logout = true)
            is ProfileIntent.Error -> state.copy(
                logout = false,
                error = UiError(true, intent.errorMsg),
                loading = true
            )

            is ProfileIntent.SetUser -> state.copy(
                logout = false,
                user = intent.user,
                error = UiError(false, null),
                loading = false
            )

            is ProfileIntent.SetState -> {
                state.copy(user = state.user?.copy(state = intent.state))
            }

            is ProfileIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val user = getUserUseCase()
                dispatch(ProfileIntent.SetUser(user))
            }
        } catch (e: Exception) {
            dispatch(ProfileIntent.Error(e.message ?: "Error getting user"))
        }
    }

    fun onLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
        }
        dispatch(ProfileIntent.Logout)
    }
    
    fun onChangeState(uid: String, state: ProfState){
        try{
            viewModelScope.launch(Dispatchers.IO) { changeStateUseCase(uid = uid, state = state) }
            dispatch(ProfileIntent.SetState(state))
        } catch(e: Exception){
            dispatch(ProfileIntent.Error(e.message.orEmpty()))
        }
    }


    fun onCloseDialog() = dispatch(ProfileIntent.CloseError)

}