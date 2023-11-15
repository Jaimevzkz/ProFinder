package com.vzkz.profinder.ui.profile.editprofile

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UserModel
import com.vzkz.profinder.domain.usecases.GetUserDataStoreUseCase
import com.vzkz.profinder.domain.usecases.ModifyUserDataUseCase
import com.vzkz.profinder.domain.usecases.SaveUserDataStoreUseCase
import com.vzkz.profinder.ui.profile.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserDataStoreUseCase: GetUserDataStoreUseCase,
    private val modifyUserDataUseCase: ModifyUserDataUseCase,
    private val saveUserDataStoreUseCase: SaveUserDataStoreUseCase
) :
    BaseViewModel<EditProfileState, EditProfileIntent>(EditProfileState.initial) {

    override fun reduce(
        state: EditProfileState,
        intent: EditProfileIntent
    ): EditProfileState { //This function reduces each intent with a when
        return when (intent) {
            is EditProfileIntent.Error -> state.copy(
                error = Error(true, intent.errorMsg)
            )

            is EditProfileIntent.SetUserFromDS -> state.copy(
                user = intent.user,
                error = Error(false, null)
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserDataStoreUseCase().collect { user ->
                if (user.uid == "") dispatch(EditProfileIntent.Error("Couldn't find user in DataStore"))
                else dispatch(EditProfileIntent.SetUserFromDS(user))
            }
        }
    }

    fun onModifyUserData(newUser: UserModel, oldUser: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modifyUserDataUseCase(newUser, oldUser)
            } catch (e: Exception) {
                dispatch(EditProfileIntent.Error("${e.message}"))
            }
            saveUserDataStoreUseCase(newUser)
        }

    }


}