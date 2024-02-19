
package com.vzkz.profinder.ui.profile.viewprofile

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.UiError


data class ViewProfileState(
    val loading: Boolean,
    val error: UiError,
    val userToSee: ActorModel?,
    val isFavourite: Boolean
) : State {
    companion object {
        val initial: ViewProfileState = ViewProfileState(
            loading = false,
            error = UiError(false, null),
            userToSee = null,
            isFavourite = false
        )
    }
}

sealed class ViewProfileIntent: Intent {
    data object Loading: ViewProfileIntent()
    data class Error(val errorMsg: String): ViewProfileIntent()
    data object CloseError: ViewProfileIntent()
    data class Setuser(val user: ActorModel): ViewProfileIntent()
    data class ChangeFavourite(val newValue: Boolean): ViewProfileIntent()
}