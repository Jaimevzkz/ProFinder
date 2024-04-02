package com.vzkz.profinder.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.core.Constants.CATEGORY
import com.vzkz.profinder.core.Constants.CONNECTION_ERROR
import com.vzkz.profinder.core.Constants.DESCRIPTION
import com.vzkz.profinder.core.Constants.FAVOURITES
import com.vzkz.profinder.core.Constants.FIRSTNAME
import com.vzkz.profinder.core.Constants.INSERTION_ERROR
import com.vzkz.profinder.core.Constants.IS_ACTIVE
import com.vzkz.profinder.core.Constants.IS_USER
import com.vzkz.profinder.core.Constants.LASTNAME
import com.vzkz.profinder.core.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.core.Constants.NAME
import com.vzkz.profinder.core.Constants.NICKNAME
import com.vzkz.profinder.core.Constants.NICKNAME_IN_USE
import com.vzkz.profinder.core.Constants.NONEXISTENT_SERVICEATTRIBUTE
import com.vzkz.profinder.core.Constants.NONEXISTENT_USERFIELD
import com.vzkz.profinder.core.Constants.NULL_USERDATA
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.PROFESSION
import com.vzkz.profinder.core.Constants.PROFILEPHOTO
import com.vzkz.profinder.core.Constants.SERVICES_COLLECTION
import com.vzkz.profinder.core.Constants.SERV_DESCRIPTION
import com.vzkz.profinder.core.Constants.STATE
import com.vzkz.profinder.core.Constants.UID
import com.vzkz.profinder.core.Constants.USERS_COLLECTION
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.RequestModel
import com.vzkz.profinder.domain.model.ServiceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreService @Inject constructor(firestore: FirebaseFirestore) {
    //Users
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun nicknameExists(nickname: String): Boolean {
        val userInfo =
            usersCollection.whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: ActorModel?) {
        if (userData == null) throw Exception(NULL_USERDATA)

        val userDocument = usersCollection.document(userData.uid)

        val userMap: Map<String, Any?> = userData.toMap()

        //SetOptions.merge() used to add the user without overriding other potential fields
        userDocument.set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                Log.i("Jaime", "Success inserting user in database")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Failure inserting user: ${it.message}")
                throw Exception(INSERTION_ERROR)
            }

    }

    suspend fun getUserData(uid: String): ActorModel {
        return try {
            val userDocumentRef = usersCollection.document(uid)

            val userDoc = userDocumentRef.get().await()

            val userData = userDoc.data

            if (userData != null) {
                val isUser = userData[IS_USER] as Boolean
                val description: String? =
                    if (userData[DESCRIPTION] == "-") null else userData[DESCRIPTION] as String
                val profession = if (isUser) null else {
                    when (userData[PROFESSION]) {
                        Professions.Plumber.name -> Professions.Plumber
                        Professions.Hairdresser.name -> Professions.Hairdresser
                        Professions.Electrician.name -> Professions.Electrician
                        else -> throw Exception(NONEXISTENT_USERFIELD)
                    }
                }
                val state = if (isUser) null else {
                    when (userData[STATE]) {
                        ProfState.Active.name -> ProfState.Active
                        ProfState.Working.name -> ProfState.Working
                        ProfState.Inactive.name -> ProfState.Inactive
                        else -> throw Exception(NONEXISTENT_USERFIELD)
                    }
                }
                val profilePicture = userData.getOrDefault(PROFILEPHOTO, null) as String?
                try {
                    val userModel = ActorModel(
                        uid = uid,
                        nickname = userData[NICKNAME] as String,
                        firstname = userData[FIRSTNAME] as String,
                        lastname = userData[LASTNAME] as String,
                        actor = if (isUser) Actors.User else Actors.Professional,
                        description = description,
                        profession = profession,
                        state = state,
                        profilePhoto = profilePicture?.let { Uri.parse(it) },
                    )

                    userModel
                } catch (e: Exception) {
                    Log.e("Jaime", "error fetching user data from db. ${e.message}")
                    throw Exception(NONEXISTENT_USERFIELD)
                }
            } else throw Exception(CONNECTION_ERROR)

        } catch (e: Exception) {
            Log.e("Jaime", "error getting user doc. ${e.message}")
            throw e
        }
    }

    suspend fun modifyUserData(
        oldUser: ActorModel,
        newUser: ActorModel
    ) { //todo this functionality does not work
        if ((oldUser.nickname != newUser.nickname) && (nicknameExists(newUser.nickname))) {
            throw Exception(NICKNAME_IN_USE)
        }

        // Gets the reference to the user document
        val userDocumentReference = usersCollection.document(oldUser.uid)

        // Casts the new UserModel to map in order to update only the needed fields
        val newUserMap = newUser.toMap()

        // Updates firestore data and wait for the update to complete
        userDocumentReference.update(newUserMap)
            .addOnSuccessListener {
                Log.i("Jaime", "User data updated successfully")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating user data: ${it.message}")
                throw Exception(MODIFICATION_ERROR)
            }
    }

    fun changeProfState(uid: String, state: ProfState) {
        usersCollection.document(uid).update(STATE, state.name)
            .addOnSuccessListener {
                Log.i("Jaime", "State updated successfully")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating state: ${it.message}")
                throw Exception(MODIFICATION_ERROR)
            }
    }

    fun changeFavouritesList(uidListOwner: String, uidToChange: String, add: Boolean) {
        val docRef = usersCollection.document(uidListOwner)
        if (add) {
            docRef.update(FAVOURITES, FieldValue.arrayUnion(uidToChange))
                .addOnSuccessListener {
                    Log.i("Jaime", "Favourite added successfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error adding favourite: ${it.message}")
                    throw Exception(MODIFICATION_ERROR)
                }
        } else {
            docRef.update(FAVOURITES, FieldValue.arrayRemove(uidToChange))
                .addOnSuccessListener {
                    Log.i("Jaime", "Favourite removed successfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error removing favourite: ${it.message}")
                    throw Exception(MODIFICATION_ERROR)
                }
        }
    }

    suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean {
        val doc = usersCollection.document(uidListOwner).get().await()
        val favList = doc[FAVOURITES] as List<String>? ?: emptyList()
        return favList.contains(uidToCheck)
    }

    suspend fun getFavouritesList(uid: String): List<ActorModel> {
        val doc = usersCollection.document(uid).get().await()
        val favList = doc[FAVOURITES] as List<String>?
        val favActorList = mutableListOf<ActorModel>()
        if (favList != null) {
            for (fav in favList) {
                favActorList.add(getUserData(fav))
            }
        }
        return favActorList.toList()
    }

    fun changeProfilePicture(uid: String, uri: Uri) {
        usersCollection.document(uid).update(PROFILEPHOTO, uri)
            .addOnSuccessListener {
                Log.i("Jaime", "Profile picture updated successfully")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating profile picture: ${it.message}")
                throw Exception(MODIFICATION_ERROR)
            }
    }

    //Services
    private val servicesCollection = firestore.collection(SERVICES_COLLECTION)

    suspend fun getServiceListByUid(uid: String): List<ServiceModel> {
        val querySnapshot = servicesCollection
            .whereEqualTo(UID, uid)
            .get()
            .await()
        return fillList(querySnapshot)
    }

    suspend fun getActiveServiceList(): List<ServiceModel> {
        val querySnapshot = servicesCollection
            .whereEqualTo(IS_ACTIVE, true)
            .get()
            .await()
        return fillList(querySnapshot)
    }

    private suspend fun fillList(querySnapshot: QuerySnapshot): List<ServiceModel> {
        val serviceList = mutableListOf<ServiceModel>()
        for (document in querySnapshot.documents) {
            val category = when (document.getString(CATEGORY)) {
                Categories.Beauty.name -> Categories.Beauty
                Categories.Household.name -> Categories.Household
                else -> throw Exception(NONEXISTENT_SERVICEATTRIBUTE)
            }
            val ownerUid = document.getString(UID) ?: throw Exception(NONEXISTENT_SERVICEATTRIBUTE)
            val owner = getUserData(ownerUid)

            serviceList.add(
                ServiceModel(
                    sid = document.id,
                    uid = ownerUid,
                    name = document.getString(NAME)
                        ?: throw Exception(NONEXISTENT_SERVICEATTRIBUTE),
                    isActive = document.getBoolean(IS_ACTIVE) ?: throw Exception(
                        NONEXISTENT_SERVICEATTRIBUTE
                    ),
                    category = category,
                    servDescription = document.getString(SERV_DESCRIPTION) ?: throw Exception(
                        NONEXISTENT_SERVICEATTRIBUTE
                    ),
                    price = document.getLong(PRICE)?.toDouble() ?: throw Exception(
                        NONEXISTENT_SERVICEATTRIBUTE
                    ),
                    owner = owner
                )
            )
        }
        return serviceList.toList()
    }

    fun insertService(service: ServiceModel) {
        val serviceDocument = servicesCollection.document()

        val serviceMap: Map<String, Any?> = service.toMap()

        //SetOptions.merge() used to add the user without overriding other potential fields
        serviceDocument.set(serviceMap, SetOptions.merge())
            .addOnSuccessListener {
                Log.i("Jaime", "Success inserting service in database")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Failure inserting service: ${it.message}")
                throw it
            }
    }

    fun deleteService(sid: String) {
        servicesCollection.document(sid).delete()
            .addOnSuccessListener {
                Log.i("Jaime", "Service deleted correctly")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error deleting service: ${it.message}")
                throw it
            }
    }

    fun modifyServiceActivity(sid: String, newValue: Boolean) {
        servicesCollection.document(sid).update(IS_ACTIVE, newValue)
            .addOnSuccessListener {
                Log.i("Jaime", "Service activity modified correctly")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error modifying service activity: ${it.message}")
                throw it
            }
    }

    //Requests
    fun getJobRequests(uid: String): Flow<List<RequestModel>> = callbackFlow {
        val requestList = mutableListOf<RequestModel>()
        usersCollection.document(uid).collection("requests").addSnapshotListener { value, error ->
            requestList.clear()
            value?.documents?.forEach { docSnapshot ->
                requestList.add(RequestModel())
            }
            trySend(requestList)
        }

    }


}
