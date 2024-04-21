package com.vzkz.profinder.data

import android.content.Context
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.data.dto.IndiviualChatDto
import com.vzkz.profinder.data.dto.JobDto
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.data.firebase.RealtimeService
import com.vzkz.profinder.data.firebase.StorageService
import com.vzkz.profinder.domain.Repository
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
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService,
    private val realtimeService: RealtimeService,
    private val context: Context,
    private val uidCombiner: UidCombiner,
    private val locationService: ILocationService
) : Repository {

    //Firebase
    override suspend fun login(email: String, password: String): Result<ActorModel, FirebaseError> {
        val user: FirebaseUser?
        try {
            user = authService.login(email, password)
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Authentication.WRONG_EMAIL_OR_PASSWORD)
        }
        return if (user != null) {
            when (val userFromFirestore = getUserFromFirestore(user.uid)) {
                is Result.Error -> return Result.Error(userFromFirestore.error)
                is Result.Success -> {
                    Result.Success(userFromFirestore.data)
                }
            }
        } else Result.Error(FirebaseError.Firestore.USER_NOT_FOUND_IN_DATABASE)
    }

    override suspend fun getUserFromFirestore(uid: String): Result<ActorModel, FirebaseError.Firestore> {
        return when (val userData = firestoreService.getUserData(uid)) {
            is Result.Success -> {
                val profilePhoto = storageService.getProfilePhoto(uid)
                Result.Success(userData.data.copy(profilePhoto = profilePhoto))
            }

            is Result.Error -> Result.Error(userData.error)
        }

    }

    override suspend fun getServiceListByUidFromFirestore(uid: String): Result<List<ServiceModel>, FirebaseError.Firestore> {
        return when (val serviceList = firestoreService.getServiceListByUid(uid)) {
            is Result.Success -> Result.Success(serviceList.data)

            is Result.Error -> Result.Error(serviceList.error)
        }
    }

    override suspend fun getActiveServiceListFromFirestore(): Result<List<ServiceModel>, FirebaseError.Firestore> {
        return when (val activeServices = firestoreService.getActiveServiceList()) {
            is Result.Success -> Result.Success(activeServices.data)

            is Result.Error -> Result.Error(activeServices.error)
        }
    }


    override fun insertServiceInFirestore(service: ServiceModel): Result<Unit, FirebaseError.Firestore> {
        return when (val serviceFromFirestore = firestoreService.insertService(service)) {
            is Result.Success -> Result.Success(serviceFromFirestore.data)

            is Result.Error -> Result.Error(serviceFromFirestore.error)
        }
    }

    override fun deleteService(sid: String): Result<Unit, FirebaseError.Firestore> {
        return when (val deletion = firestoreService.deleteService(sid)) {
            is Result.Success -> Result.Success(deletion.data)

            is Result.Error -> Result.Error(deletion.error)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel, FirebaseError> {
        if (firestoreService.nicknameExists(nickname)) {
            return Result.Error(FirebaseError.Authentication.USERNAME_ALREADY_IN_USE)
        } else {
            val user: ActorModel
            try {
                val firebaseUser = authService.signUp(email, password)
                if (firebaseUser != null) {
                    user = ActorModel(
                        nickname = nickname,
                        uid = firebaseUser.uid,
                        firstname = firstname,
                        lastname = lastname,
                        actor = actor,
                        profession = profession
                    )
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                return Result.Error(FirebaseError.Authentication.ACCOUNT_WITH_THAT_EMAIL_ALREADY_EXISTS)
            }
            return when (val userFromFirestore = firestoreService.insertUser(user)) {
                is Result.Success -> Result.Success(user)
                is Result.Error -> Result.Error(userFromFirestore.error)
            }
        }
    }

    override suspend fun logout() = authService.logout()

    override fun isUserLogged() = authService.isUserLogged()

    override suspend fun modifyUserData(
        oldUser: ActorModel,
        newUser: ActorModel
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val modification =
            firestoreService.modifyUserData(oldUser = oldUser, newUser = newUser)) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override fun changeProfState(
        uid: String,
        state: ProfState
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val modification = firestoreService.changeProfState(uid, state)) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override fun modifyServiceActivity(
        sid: String,
        newValue: Boolean
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val modification = firestoreService.modifyServiceActivity(sid, newValue)) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override fun changeFavouriteList(
        uidListOwner: String,
        uidToChange: String,
        add: Boolean
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val modification = firestoreService.changeFavouritesList(
            uidListOwner = uidListOwner,
            uidToChange = uidToChange,
            add = add
        )) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean =
        firestoreService.checkIsFavourite(
            uidListOwner = uidListOwner,
            uidToCheck = uidToCheck
        )


    override suspend fun getFavouriteList(uid: String): Result<List<ActorModel>, FirebaseError.Firestore> {
        return when (val favList = firestoreService.getFavouritesList(uid)) {
            is Result.Success -> Result.Success(favList.data)
            is Result.Error -> Result.Error(favList.error)
        }
    }

    override fun updateRating(uid: String, newRating: Int): Result<Unit, FirebaseError.Firestore> {
        return when (val modification =
            firestoreService.updateRating(uid = uid, newRating = newRating)) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override fun getJobsOrRequests(
        isRequest: Boolean,
        uid: String
    ): Result<Flow<List<JobModel>>, FirebaseError.Firestore> {
        return try {
            val flow = firestoreService.getJobsOrRequests(isRequest = isRequest, uid = uid)
            Result.Success(flow)
        } catch (e: Exception) {
            Result.Error(FirebaseError.Firestore.NONEXISTENT_REQUEST_ATTRIBUTE)
        }
    }

    override fun addJobOrRequest(
        isRequest: Boolean,
        profUid: String,
        profNickname: String,
        clientNickname: String,
        clientId: String,
        serviceName: String,
        serviceId: String,
        price: Double
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val addition =
            firestoreService.addNewJobOrRequest(
                isRequest = isRequest,
                profUid = profUid,
                request = JobDto(
                    profNickname = profNickname,
                    otherNickname = clientNickname,
                    otherId = clientId,
                    serviceName = serviceName,
                    serviceId = serviceId,
                    price = price
                )
            )) {
            is Result.Success -> Result.Success(addition.data)
            is Result.Error -> Result.Error(addition.error)
        }
    }

    override fun deleteJobOrRequest(
        isRequest: Boolean,
        uid: String,
        otherUid: String,
        id: String
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val deletion =
            firestoreService.deleteJobOrRequest(
                isRequest = isRequest,
                uid = uid,
                otherUid = otherUid,
                id = id
            )) {
            is Result.Success -> Result.Success(deletion.data)
            is Result.Error -> Result.Error(deletion.error)
        }
    }

    override fun deleteIndividualJob(uid: String, jid: String) = firestoreService.deleteIndividualJob(uid = uid, jid = jid)

    override fun turnRequestIntoJob(
        ownerNickname: String,
        uid: String,
        request: JobModel
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val modification =
            firestoreService.turnRequestIntoJob(
                request.id,
                uid = uid,
                request = JobDto(
                    profNickname = ownerNickname,
                    otherNickname = request.otherNickname,
                    otherId = request.otherUid,
                    serviceName = request.serviceName,
                    serviceId = request.serviceId,
                    price = request.price
                )
            )) {
            is Result.Success -> Result.Success(modification.data)
            is Result.Error -> Result.Error(modification.error)
        }
    }

    override fun setRatingPending(uid: String, jid: String) = firestoreService.setRatingPending(uid = uid, jid = jid)

    //Storage
    override suspend fun uploadAndDownloadProfilePhoto(
        uri: Uri,
        uid: String,
        oldProfileUri: Uri?
    ): Uri {
        val storageUri = storageService.uploadAndDownloadProgressPhoto(
            uri = uri,
            uid = uid,
            oldProfileUri = oldProfileUri
        )
        firestoreService.changeProfilePicture(uid, storageUri)
        return storageUri
    }

    //Realtime
    override fun getRecentChats(uid: String): Result<Flow<List<ChatListItemModel>>, FirebaseError.Realtime> {
        return try {
            Result.Success(realtimeService.getRecentChats(uid))
        } catch (e: Exception) {
            Result.Error(FirebaseError.Realtime.ERROR_GETTING_RECENT_CHATS)
        }
    }

    override fun addRecentChat(
        chatListItemModel: ChatListItemModel,
        ownerUid: String,
        ownerNickname: String,
        ownerProfilePhoto: Uri?
    ) {
        val participants: Map<String, ParticipantDataDto> = mapOf(
            Pair(
                ownerUid,
                ParticipantDataDto(
                    profilePhoto = ownerProfilePhoto?.toString(),
                    nickname = ownerNickname
                )
            ),
            Pair(
                chatListItemModel.uid,
                ParticipantDataDto(
                    profilePhoto = chatListItemModel.profilePhoto?.toString(),
                    nickname = chatListItemModel.nickname
                )
            ),

            )
        val recentChatDto = RecentChatDto(
            participants = participants,
            timestamp = chatListItemModel.timestamp,
            lastMsg = chatListItemModel.lastMsg,
            unreadMsgNumber = chatListItemModel.unreadMsgNumber,
            lastMsgUid = chatListItemModel.lastMsgUid,
        )
        realtimeService.addOrModifyRecentChat(
            combinedUid = uidCombiner.combineUids(ownerUid, chatListItemModel.uid),
            chatDto = recentChatDto
        )
    }

    override fun updateRecentChat(
        combinedUid: String,
        message: String,
        timestamp: Long,
        senderUid: String,
        participants: Map<String, ParticipantDataDto>
    ): Result<Unit, FirebaseError.Realtime> {
        return when (val update = realtimeService.updateRecentChats(
            combinedUids = combinedUid,
            message = message,
            timestamp = timestamp,
            senderUid = senderUid,
            participants = participants
        )) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(update.error)
        }
    }

    override fun openRecentChat(combinedUid: String) =
        realtimeService.openRecentChat(combinedUid = combinedUid)


    override fun getUnreadMsgAndOwner(
        ownerUid: String,
        combinedUid: String
    ): Result<Flow<Pair<Boolean, Int>>, FirebaseError.Realtime> {
        return try {
            val flow =
                realtimeService.getUnreadMsgAndOwner(ownerUid = ownerUid, combinedUid = combinedUid)
            Result.Success(flow)
        } catch (e: Exception) {
            Result.Error(FirebaseError.Realtime.ERROR_GETTING_RECENT_CHATS)
        }
    }

    override fun getIndividualChat(
        ownerUid: String,
        otherUid: String
    ): Result<Flow<List<ChatMsgModel>>, FirebaseError.Realtime> {
        return try {
            val flow =
                realtimeService.getChats(
                    combinedUid = uidCombiner.combineUids(ownerUid, otherUid),
                    ownerUid = ownerUid
                )
            Result.Success(flow)
        } catch (e: Exception) {
            Result.Error(FirebaseError.Realtime.ERROR_GETTING_RECENT_CHATS)
        }
    }

    override fun addNewMessage(ownerUid: String, otherUid: String, chatMsgModel: ChatMsgModel) =
        realtimeService.addNewMessage(
            uidCombiner.combineUids(ownerUid, otherUid),
            indiviualChatDto = IndiviualChatDto(
                message = chatMsgModel.msg,
                timestamp = chatMsgModel.timestamp,
                senderUid = if (chatMsgModel.isMine) ownerUid else otherUid
            )
        )

    //location
    override suspend fun getLocation(): Flow<LatLng?> = locationService.requestLocationUpdates()

    override suspend fun updateFirestoreLocation(uid: String, nickname: String) {
        locationService.requestLocationUpdates().collect { location ->
            if (location != null)
                firestoreService.updateUserLocation(uid = uid, location = location, nickname = nickname)
        }
    }

    override fun getLocations(
        uid: String
    ): Result<Flow<List<LocationModel>>, FirebaseError.Firestore> {
        return try {
            val flow = firestoreService.getLocations(uid)
            Result.Success(flow)
        } catch (e: Exception) {
            Result.Error(FirebaseError.Firestore.ERROR_GETTING_LOCATIONS)
        }
    }
}

