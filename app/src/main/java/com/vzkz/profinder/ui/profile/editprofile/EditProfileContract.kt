package com.vzkz.profinder.ui.profile.editprofile

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.ui.UiText


data class EditProfileState(
    val user: ActorModel?,
    val error: UiText?,
    val success: Boolean,
    val loading: Boolean,
    val wholeScreenLoading: Boolean
) : State {
    companion object {
        val initial: EditProfileState = EditProfileState(
            user = null,
            error = null,
            success = false,
            loading = false,
            wholeScreenLoading = true
        )
    }
}
sealed class EditProfileIntent: IndividualChatntent {
    data class SetUser(val user: ActorModel?): EditProfileIntent()
    data class Error(val error: UiText): EditProfileIntent()
    data object CloseError: EditProfileIntent()
    data object  Success: EditProfileIntent()
    data object Loading: EditProfileIntent()
    data class SetImg(val updatedUser: ActorModel): EditProfileIntent()
}