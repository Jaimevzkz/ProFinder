package com.vzkz.profinder.domain

import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel

interface Repository {

    suspend fun login(email: String, password: String): Result<ActorModel>

    suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel>

    suspend fun logout()

    fun isUserLogged(): Boolean

    suspend fun modifyUserData(oldUser: ActorModel, newUser: ActorModel)

    fun changeProfState(uid: String, state: ProfState)

    suspend fun getUserFromFirestore(uid: String): ActorModel

    suspend fun getServiceListFromFirestore(uid: String): List<ServiceModel>

    fun insertServiceInFirestore(service: ServiceModel)

    fun deleteService(sid: String)

    fun modifyServiceActivity(sid: String, newValue: Boolean)

}