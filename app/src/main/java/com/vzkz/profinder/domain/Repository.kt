package com.vzkz.profinder.domain

import android.net.Uri
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

    suspend fun getServiceListByUidFromFirestore(uid: String): List<ServiceModel>

    suspend fun getActiveServiceListFromFirestore(): List<ServiceModel>

    fun insertServiceInFirestore(service: ServiceModel)

    fun deleteService(sid: String)

    fun modifyServiceActivity(sid: String, newValue: Boolean)

    fun changeFavouriteList(uidListOwner: String, uidToChange: String, add: Boolean)

    suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean

    suspend fun getFavouriteList(uid: String): List<ActorModel>

    suspend fun uploadAndDownloadProfilePhoto(uri: Uri, uid: String, oldProfileUri: Uri?): Uri

}