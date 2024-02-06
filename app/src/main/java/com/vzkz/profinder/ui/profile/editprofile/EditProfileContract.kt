package com.vzkz.profinder.ui.profile.editprofile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError


data class EditProfileState(
    val user: ActorModel?,
    val error: UiError,
    val success: Boolean,
    val loading: Boolean
) : State {
    companion object {
        val initial: EditProfileState = EditProfileState(
            user = null,
            error = UiError(false, null),
            success = false,
            loading = true
        )
    }
}
sealed class EditProfileIntent: Intent {
    data class SetUser(val user: ActorModel?): EditProfileIntent()
    data class Error(val errorMsg: String): EditProfileIntent()
    data object CloseError: EditProfileIntent()
    data object  Success: EditProfileIntent()
    data object Loading: EditProfileIntent()
}