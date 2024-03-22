package com.vzkz.profinder.data

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vzkz.profinder.R
import com.vzkz.profinder.core.Constants.CONNECTION_ERROR
import com.vzkz.profinder.core.Constants.INSERTION_ERROR
import com.vzkz.profinder.core.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.core.Constants.NICKNAME_IN_USE
import com.vzkz.profinder.core.Constants.NONEXISTENT_SERVICEATTRIBUTE
import com.vzkz.profinder.core.Constants.NONEXISTENT_USERFIELD
import com.vzkz.profinder.core.Constants.NULL_USERDATA
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.firebase.AuthService
import com.vzkz.profinder.data.firebase.FirestoreService
import com.vzkz.profinder.data.firebase.RealtimeService
import com.vzkz.profinder.data.firebase.StorageService
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService,
    private val realtimeService: RealtimeService,
    private val context: Context
) : Repository {

    //Firebase
    override suspend fun login(email: String, password: String): Result<ActorModel> {
        val user: FirebaseUser?
        try {
            user = authService.login(email, password)
        } catch (e: Exception) {
            return Result.failure(Exception(context.getString(R.string.wrong_email_or_password)))
        }
        return if (user != null) {
            Result.success(getUserFromFirestore(user.uid))
        } else Result.failure(Exception(context.getString(R.string.error_logging_user)))
    }

    override suspend fun getUserFromFirestore(uid: String): ActorModel {
        try {
            return firestoreService.getUserData(uid).copy(
                profilePhoto = storageService.getProfilePhoto(uid)
            )
        } catch (e: Exception) {
            throw handleException(e)
        }
    }

    override suspend fun getServiceListByUidFromFirestore(uid: String): List<ServiceModel> {
        return try {
            firestoreService.getServiceListByUid(uid)
        } catch (e: Exception) {
            throw handleException(e)
        }
    }

    override suspend fun getActiveServiceListFromFirestore(): List<ServiceModel> {
        return try {
            firestoreService.getActiveServiceList()
        } catch (e: Exception) {
            throw handleException(e)
        }
    }


    override fun insertServiceInFirestore(service: ServiceModel) =
        firestoreService.insertService(service)//throws exception

    override fun deleteService(sid: String) =
        firestoreService.deleteService(sid) //throws exception

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel> {
        if (firestoreService.nicknameExists(nickname)) {
            return Result.failure(Exception(context.getString(R.string.username_already_in_use)))
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
                return Result.failure(Exception(context.getString(R.string.account_already_exists)))
            }
            return try {
                firestoreService.insertUser(user)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(Exception(context.getString(R.string.couldn_t_insert_user_in_database)))
            }
        }
    }

    override suspend fun logout() = authService.logout()

    override fun isUserLogged() = authService.isUserLogged()
    override suspend fun modifyUserData(oldUser: ActorModel, newUser: ActorModel) {
        try {
            firestoreService.modifyUserData(oldUser = oldUser, newUser = newUser)
        } catch (e: Exception) {
            throw handleException(e)
        }
    }

    override fun changeProfState(uid: String, state: ProfState) =
        firestoreService.changeProfState(uid, state) //throws exception

    override fun modifyServiceActivity(sid: String, newValue: Boolean) =
        firestoreService.modifyServiceActivity(sid, newValue) //throws exception

    override fun changeFavouriteList(uidListOwner: String, uidToChange: String, add: Boolean) {
        firestoreService.changeFavouritesList(
            uidListOwner = uidListOwner,
            uidToChange = uidToChange,
            add = add
        )
    }

    override suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean {
        return firestoreService.checkIsFavourite(
            uidListOwner = uidListOwner,
            uidToCheck = uidToCheck
        )
    }

    override suspend fun getFavouriteList(uid: String): List<ActorModel> =
        firestoreService.getFavouritesList(uid) //throws exception

    //Storage
    override suspend fun uploadAndDownloadProfilePhoto(
        uri: Uri,
        uid: String,
        oldProfileUri: Uri?
    ): Uri { //throws exception
        val storageUri = storageService.uploadAndDownloadProgressPhoto(
            uri = uri,
            uid = uid,
            oldProfileUri = oldProfileUri
        )
        firestoreService.changeProfilePicture(uid, storageUri)
        return storageUri
    }

    //Realtime
    override fun getRecentChats(uid: String) = realtimeService.getRecentChats(uid)

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
            chatId = chatListItemModel.chatId
        )
        realtimeService.addOrModifyRecentChat(
            chatId = chatListItemModel.chatId,
            chatDto = recentChatDto
        )

    }

    //Exception handling
    private fun handleException(e: Exception): Exception {
        return when (e.message) {
            CONNECTION_ERROR -> Exception(context.getString(R.string.network_failure_while_checking_user_existence))
            NICKNAME_IN_USE -> Exception(context.getString(R.string.nickname_already_in_use_couldn_t_modify_user))
            MODIFICATION_ERROR -> Exception(context.getString(R.string.error_modifying_user_data_the_user_wasn_t_modified))
            NULL_USERDATA -> Exception(context.getString(R.string.user_not_found_in_database))
            INSERTION_ERROR -> Exception(context.getString(R.string.couldn_t_insert_user_in_database))
            NONEXISTENT_USERFIELD -> Exception(context.getString(R.string.needed_values_missing_in_database))
            NONEXISTENT_SERVICEATTRIBUTE -> Exception(context.getString(R.string.attribute_of_a_service_corrupted_in_the_database))
            else -> Exception(context.getString(R.string.unknown_exception_occurred))
        }
    }
}

