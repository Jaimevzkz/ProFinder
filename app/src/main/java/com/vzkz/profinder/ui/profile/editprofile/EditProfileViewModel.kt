package com.vzkz.profinder.ui.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.user.ModifyUserDataUseCase
import com.vzkz.profinder.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val modifyUserDataUseCase: ModifyUserDataUseCase,
    private val getUserUseCase: GetUserUseCase
) :
    BaseViewModel<EditProfileState, EditProfileIntent>(EditProfileState.initial) {

    override fun reduce(
        state: EditProfileState,
        intent: EditProfileIntent
    ): EditProfileState { //This function reduces each intent with a when
        return when (intent) {
            is EditProfileIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg),
                success = false,
                loading = true
            )

            is EditProfileIntent.SetUser -> state.copy(
                user = intent.user,
                success = false,
                loading = false
            )

            EditProfileIntent.CloseError -> state.copy(
                error = Error(false, null),
                success = true
            )

            EditProfileIntent.Success -> state.copy(
                error = Error(false, null),
                success = false
            )

            is EditProfileIntent.Loading -> state.copy(
                success = false,
                loading = true
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val user = getUserUseCase()
                dispatch(EditProfileIntent.SetUser(user))
            }
        } catch (e: Exception) {
            dispatch(EditProfileIntent.Error(e.message ?: "Error getting user"))
        }
    }

    fun onModifyUserData(newUser: ActorModel, oldUser: ActorModel) {
        dispatch(EditProfileIntent.Loading)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    modifyUserDataUseCase(
                        newUser = newUser,
                        oldUser = oldUser
                    )
                    dispatch(EditProfileIntent.Success)
                }

            } catch (e: Exception) {
                dispatch(EditProfileIntent.Error("${e.message}"))
            }
        }

    }

    fun onCloseDialog() = dispatch(EditProfileIntent.CloseError)


}