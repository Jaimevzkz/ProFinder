
package com.vzkz.profinder.ui.profile.viewprofile

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError


data class ViewProfileState(
    val loading: Boolean,
    val error: UiError,
    val userToSee: ActorModel?,
    val isFavourite: Boolean,
    val uid: String?
) : State {
    companion object {
        val initial: ViewProfileState = ViewProfileState(
            loading = true,
            error = UiError(false, null),
            userToSee = null,
            isFavourite = false,
            uid = null
        )
    }
}

sealed class ViewProfileIntent: IndividualChatntent {
    data object Loading: ViewProfileIntent()
    data class Error(val errorMsg: String): ViewProfileIntent()
    data object CloseError: ViewProfileIntent()
    data class Setuser(val user: ActorModel): ViewProfileIntent()
    data class ChangeFavourite(val newValue: Boolean): ViewProfileIntent()
    data class SetUid(val uid: String): ViewProfileIntent()
}