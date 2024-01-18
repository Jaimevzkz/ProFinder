package com.vzkz.profinder.domain

import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Professions

interface Repository {

    suspend fun login(email: String, password: String): ActorModel?

    suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): ActorModel?

    suspend fun logout()

    fun isUserLogged(): Boolean

    suspend fun modifyUserData(oldUser: ActorModel, newUser: ActorModel)

    suspend fun getUserFromFirestore(uid: String): ActorModel

}