package com.vzkz.profinder.domain.model

import android.net.Uri
import com.vzkz.profinder.core.Constants.DESCRIPTION
import com.vzkz.profinder.core.Constants.FIRSTNAME
import com.vzkz.profinder.core.Constants.IS_USER
import com.vzkz.profinder.core.Constants.LASTNAME
import com.vzkz.profinder.core.Constants.NICKNAME
import com.vzkz.profinder.core.Constants.PROFESSION
import com.vzkz.profinder.core.Constants.STATE

data class ActorModel(
    val nickname: String,
    val uid: String,
    val firstname: String,
    val lastname: String,
    val description: String? = null,
    val actor: Actors,
    val profession: Professions? = null, //Only for professionals
    val state: ProfState? = null, //Only for professionals
    val profilePhoto: Uri? = null,
    val favourites: List<String> = emptyList()
){
    constructor(): this(
        nickname = "",
        uid = "",
        firstname = "",
        lastname = "",
        actor = Actors.User
    )
    fun toMap(): Map<String, Any?> {
        val isUser = actor == Actors.User
        val descriptionToInsert = description ?: "-"
        return if(isUser){
            mapOf(
                NICKNAME to nickname,
                FIRSTNAME to firstname,
                IS_USER to true,
                LASTNAME to lastname,
                DESCRIPTION to descriptionToInsert
            )
        } else{
            val stateToInsert = state?.name ?: ProfState.Inactive
            mapOf(
                NICKNAME to nickname,
                FIRSTNAME to firstname,
                IS_USER to false,
                LASTNAME to lastname,
                DESCRIPTION to descriptionToInsert,
                PROFESSION to profession,
                STATE to stateToInsert
            )
        }
    }
}