package com.vzkz.profinder.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.auth.LoginUseCase
import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCase
import com.vzkz.profinder.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveUidDataStoreUseCase: SaveUidDataStoreUseCase,
    private val dispatcher: CoroutineDispatcher
) :
    BaseViewModel<LoginState, LoginIntent>(LoginState.initial) {

    override fun reduce(
        state: LoginState,
        intent: LoginIntent
    ): LoginState { //This function reduces each intent with a when
        return when (intent) {
            is LoginIntent.Loading -> state.copy(
                loading = intent.isLoading
            )

            is LoginIntent.Login -> state.copy(
                user = intent.user,
                loading = false,
                success = true
            )

            is LoginIntent.Error -> state.copy(
                error = intent.error,
                loading = false,
                success = false
            )

            LoginIntent.CloseError -> state.copy(
                error = null,
                loading = false,
                success = false
            )

        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onLogin(email: String, password: String) {
        dispatch(LoginIntent.Loading(isLoading = true))
        viewModelScope.launch(dispatcher) {
            when(val login = loginUseCase(email, password)){
                is Result.Success -> {
                    withContext(Dispatchers.IO) { saveUidDataStoreUseCase(login.data.uid) }
                    dispatch(LoginIntent.Login(login.data))
                }
                is Result.Error -> dispatch(LoginIntent.Error(login.error.asUiText()))
            }
        }
    }

    fun onCloseDialog() = dispatch(LoginIntent.CloseError)


}