package com.vzkz.profinder.ui.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val saveUidDataStoreUseCase: SaveUidDataStoreUseCase
) : BaseViewModel<SignUpState, SignUpIntent>(SignUpState.initial) {

    override fun reduce(
        state: SignUpState,
        intent: SignUpIntent
    ): SignUpState { //This function reduces each intent with a when
        return when (intent) {
            is SignUpIntent.Loading -> state.copy(
                loading = intent.isLoading
            )

            is SignUpIntent.SignUp -> state.copy(
                error = Error(false, null),
                user = intent.user,
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
    fun onSignUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ) {
        dispatch(SignUpIntent.Loading(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase(
                email = email,
                password = password,
                nickname = nickname,
                actor = actor,
                firstname = firstname,
                lastname = lastname,
                profession = profession
            ).onSuccess { actor ->
                withContext(Dispatchers.IO) { saveUidDataStoreUseCase(actor.uid) }
                dispatch(SignUpIntent.SignUp(actor))

            }.onFailure { e ->
                Log.e("Jaime", e.message.orEmpty())
                dispatch(SignUpIntent.Error(e.message.orEmpty()))
            }

        }
    }

    fun onCloseDialog() = dispatch(SignUpIntent.CloseError)

}