package com.vzkz.profinder.ui.profile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.auth.LogoutUseCase
import com.vzkz.profinder.domain.usecases.user.ChangeStateUseCase
import com.vzkz.profinder.ui.asUiText
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
                error = intent.error,
                loading = true
            )

            is ProfileIntent.SetUser -> state.copy(
                logout = false,
                user = intent.user,
                loading = false
            )

            is ProfileIntent.SetState -> {
                state.copy(user = state.user?.copy(state = intent.state))
            }

            is ProfileIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val user = getUserUseCase()){
                is Result.Success -> dispatch(ProfileIntent.SetUser(user.data))
                is Result.Error -> dispatch(ProfileIntent.Error(user.error.asUiText()))
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
        }
        dispatch(ProfileIntent.Logout)
    }
    
    fun onChangeState(uid: String, state: ProfState){
        viewModelScope.launch(Dispatchers.IO) {
            when(val change = changeStateUseCase(uid = uid, state = state)){
                is Result.Success -> dispatch(ProfileIntent.SetState(state))
                is Result.Error -> dispatch(ProfileIntent.Error(change.error.asUiText()))
            }
        }
    }


    fun onCloseDialog() = dispatch(ProfileIntent.CloseError)

}