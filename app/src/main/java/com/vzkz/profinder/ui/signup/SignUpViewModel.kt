package com.vzkz.profinder.ui.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(val signUpUseCase: SignUpUseCase): BaseViewModel<SignUpState, SignUpIntent>(SignUpState.initial) {

    override fun reduce(state: SignUpState, intent: SignUpIntent): SignUpState { //This function reduces each intent with a when
        return when (intent) {
            is SignUpIntent.Loading -> state.copy(
                loading = intent.isLoading
            )

            is SignUpIntent.SignUp -> state.copy(
                error = Error(false, null),
                userName = intent.username,
                loading = false,
                success = true
            )

            is SignUpIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                loading = false,
                success = false
            )

            SignUpIntent.CloseError -> state.copy(
                error = Error(false, null),
                loading = false,
                success = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onSignUp(email: String, password: String) {
        dispatch(SignUpIntent.Loading(isLoading = true))
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { signUpUseCase(email, password) }
                if (result != null) {
                    dispatch(SignUpIntent.SignUp(result.user))
                } else {
                    Log.e("Jaime", "The code should never get here (Exception controlled)")
                    dispatch(SignUpIntent.Error(""))
                }
            } catch (e: Exception) {
                Log.e("Jaime", e.message.orEmpty())
                dispatch(SignUpIntent.Error(e.message.orEmpty()))
            }
        }

    }

    fun onCloseDialog() = dispatch(SignUpIntent.CloseError)

}