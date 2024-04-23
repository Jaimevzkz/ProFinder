package com.vzkz.profinder.ui.profile.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.user.ModifyUserDataUseCase
import com.vzkz.profinder.domain.usecases.user.UploadPhotoUseCase
import com.vzkz.profinder.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val modifyUserDataUseCase: ModifyUserDataUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) :
    BaseViewModel<EditProfileState, EditProfileIntent>(EditProfileState.initial) {

    override fun reduce(
        state: EditProfileState,
        intent: EditProfileIntent
    ): EditProfileState { //This function reduces each intent with a when
        return when (intent) {

            is EditProfileIntent.Loading -> state.copy(
                loading = !this.state.loading
            )
            is EditProfileIntent.Error -> state.copy(
                error = intent.error,
                success = false,
                loading = false
            )

            is EditProfileIntent.SetUser -> state.copy(
                user = intent.user,
                success = false,
                wholeScreenLoading = false
            )

            is EditProfileIntent.CloseError -> state.copy(
                error = null,
            )

            is EditProfileIntent.Success -> state.copy(
                success = true,
                loading = false
            )

            is EditProfileIntent.SetImg -> state.copy(user = intent.updatedUser)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val user = getUserUseCase()){
                is Result.Success -> dispatch(EditProfileIntent.SetUser(user.data))
                is Result.Error -> dispatch(EditProfileIntent.Error(user.error.asUiText()))
            }
        }
    }

    fun onModifyUserData(uid: String, changedFields: Map<String, Any>) {
        dispatch(EditProfileIntent.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            when (val modification = modifyUserDataUseCase(
                uid = uid,
                changedFields = changedFields
            )) {
                is Result.Success -> dispatch(EditProfileIntent.Success)
                is Result.Error -> dispatch(EditProfileIntent.Error(modification.error.asUiText()))
            }
        }
    }


    fun onCloseDialog() = dispatch(EditProfileIntent.CloseError)

    fun onUploadPhoto(uri: Uri, user: ActorModel) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                uploadPhotoUseCase(
                    uri = uri,
                    user = user
                )
            }
            dispatch(EditProfileIntent.SetImg(user.copy(profilePhoto = result)))
        }
    }
}