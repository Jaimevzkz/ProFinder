package com.vzkz.profinder.domain.model

import com.vzkz.profinder.domain.model.Constants.DESCRIPTION
import com.vzkz.profinder.domain.model.Constants.FIRSTNAME
import com.vzkz.profinder.domain.model.Constants.ISUSER
import com.vzkz.profinder.domain.model.Constants.LASTNAME
import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.PROFESSION
import com.vzkz.profinder.domain.model.Constants.STATE

open class ActorModel(
    val nickname: String,
    val uid: String,
    val firstname: String,
    val lastname: String,
    val description: String? = null,
    val actor: Actors,
    val profession: Professions? = null, //Only for professionals
    val state: ProfState? = null //Only for professionals
){
    fun toMap(): Map<String, Any?> {
        val isUser = actor == Actors.User
        val descriptionToInsert = description ?: "-"
        return if(isUser){
            mapOf(
                NICKNAME to nickname,
                FIRSTNAME to firstname,
                ISUSER to true,
                LASTNAME to lastname,
                DESCRIPTION to descriptionToInsert
            )
        } else{
            val stateToInsert = state?.name ?: ProfState.Inactive
            mapOf(
                NICKNAME to nickname,
                FIRSTNAME to firstname,
                ISUSER to false,
                LASTNAME to lastname,
                DESCRIPTION to description,
                PROFESSION to profession,
                STATE to stateToInsert
            )
        }

    }
}