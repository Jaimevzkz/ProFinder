package com.vzkz.profinder.domain

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.LocationModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow

interface Repository {


    suspend fun login(email: String, password: String): Result<ActorModel, FirebaseError>
    suspend fun getUserFromFirestore(uid: String): Result<ActorModel, FirebaseError.Firestore>
    suspend fun getServiceListByUidFromFirestore(uid: String): Result<List<ServiceModel>, FirebaseError.Firestore>
    suspend fun getActiveServiceListFromFirestore(): Result<List<ServiceModel>, FirebaseError.Firestore>
    fun insertServiceInFirestore(service: ServiceModel): Result<Unit, FirebaseError.Firestore>
    fun deleteService(sid: String): Result<Unit, FirebaseError.Firestore>
    suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel, FirebaseError>

    suspend fun logout()
    fun isUserLogged(): Boolean
    suspend fun modifyUserData(
        oldUser: ActorModel,
        newUser: ActorModel
    ): Result<Unit, FirebaseError.Firestore>

    fun changeProfState(uid: String, state: ProfState): Result<Unit, FirebaseError.Firestore>
    fun modifyServiceActivity(sid: String, newValue: Boolean): Result<Unit, FirebaseError.Firestore>
    fun changeFavouriteList(
        uidListOwner: String,
        uidToChange: String,
        add: Boolean
    ): Result<Unit, FirebaseError.Firestore>

    suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean
    suspend fun getFavouriteList(uid: String): Result<List<ActorModel>, FirebaseError.Firestore>
    fun updateRating(uid: String, newRating: Int): Result<Unit, FirebaseError.Firestore>
    fun getJobsOrRequests(
        isRequest: Boolean,
        uid: String
    ): Result<Flow<List<JobModel>>, FirebaseError.Firestore>

    fun addJobOrRequest(
        isRequest: Boolean,
        profUid: String,
        profNickname: String,
        clientNickname: String,
        clientId: String,
        serviceName: String,
        serviceId: String,
        price: Double
    ): Result<Unit, FirebaseError.Firestore>

    fun deleteJobOrRequest(
        isRequest: Boolean,
        uid: String,
        otherUid: String,
        id: String
    ): Result<Unit, FirebaseError.Firestore>

    fun turnRequestIntoJob(
        ownerNickname: String,
        uid: String,
        request: JobModel
    ): Result<Unit, FirebaseError.Firestore>

    suspend fun uploadAndDownloadProfilePhoto(uri: Uri, uid: String, oldProfileUri: Uri?): Uri
    fun getRecentChats(uid: String): Result<Flow<List<ChatListItemModel>>, FirebaseError.Realtime>
    fun addRecentChat(
        chatListItemModel: ChatListItemModel,
        ownerUid: String,
        ownerNickname: String,
        ownerProfilePhoto: Uri?
    )

    fun updateRecentChat(
        combinedUid: String,
        message: String,
        timestamp: Long,
        senderUid: String,
        participants: Map<String, ParticipantDataDto>
    ): Result<Unit, FirebaseError.Realtime>

    fun openRecentChat(combinedUid: String)
    fun getUnreadMsgAndOwner(
        ownerUid: String,
        combinedUid: String
    ): Result<Flow<Pair<Boolean, Int>>, FirebaseError.Realtime>

    fun getIndividualChat(
        ownerUid: String,
        otherUid: String
    ): Result<Flow<List<ChatMsgModel>>, FirebaseError.Realtime>

    fun addNewMessage(ownerUid: String, otherUid: String, chatMsgModel: ChatMsgModel)
    suspend fun getLocation(): Flow<LatLng?>
    suspend fun updateFirestoreLocation(uid: String, nickname: String)
    fun getLocations(uid: String): Result<Flow<List<LocationModel>>, FirebaseError.Firestore>
}