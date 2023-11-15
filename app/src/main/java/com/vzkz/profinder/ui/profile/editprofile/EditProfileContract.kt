package com.vzkz.profinder.ui.profile.editprofile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.UserModel
import com.vzkz.profinder.ui.profile.Error
import com.vzkz.profinder.ui.profile.ProfileIntent


data class EditProfileState(
    val user: UserModel?,
    val error: Error
) : State {
    companion object {
        val initial: EditProfileState = EditProfileState(
            user = null,
            error = Error(false, null)
        )
    }
}

data class Error(val isError: Boolean, val errorMsg: String?)

sealed class EditProfileIntent: Intent {
    data class SetUserFromDS(val user: UserModel?): EditProfileIntent()
    data class Error(val errorMsg: String): EditProfileIntent()
}