package com.vzkz.profinder.domain

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow

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

    fun getRecentChats(uid: String): Flow<List<ChatListItemModel>>
    fun addRecentChat(
        chatListItemModel: ChatListItemModel,
        ownerUid: String,
        ownerNickname: String,
        ownerProfilePhoto: Uri?
    )

    fun getIndividualChat(ownerUid: String, otherUid: String): Flow<List<ChatMsgModel>>
    fun addNewMessage(ownerUid: String, otherUid: String, chatMsgModel: ChatMsgModel)
    fun openRecentChat(combinedUid: String)
    fun updateRecentChat(
        combinedUid: String,
        message: String,
        timestamp: Long,
        senderUid: String,
        participants: Map<String, ParticipantDataDto>
    )

    fun getUnreadMsgAndOwner(ownerUid: String, combinedUid: String): Flow<Pair<Boolean, Int>>
    fun getJobsOrRequests(isRequest: Boolean, uid: String): Flow<List<JobModel>>
    fun addJobOrRequest(
        isRequest: Boolean,
        profUid: String,
        profNickname: String,
        clientNickname: String,
        clientId: String,
        serviceName: String,
        serviceId: String,
        price: Double
    )

    fun deleteJobOrRequest(
        isRequest: Boolean,
        uid: String,
        otherUid: String,
        id: String
    )

    fun turnRequestIntoJob(ownerNickname: String, uid: String, request: JobModel)
    fun updateRating(uid: String, newRating: Int)
    suspend fun getLocation(uid: String): Flow<LatLng?>}