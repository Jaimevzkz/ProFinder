package com.vzkz.profinder.data.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.vzkz.profinder.core.Constants.CATEGORY
import com.vzkz.profinder.core.Constants.DESCRIPTION
import com.vzkz.profinder.core.Constants.FAVOURITES
import com.vzkz.profinder.core.Constants.FIRSTNAME
import com.vzkz.profinder.core.Constants.IS_ACTIVE
import com.vzkz.profinder.core.Constants.IS_RATING_PENDING
import com.vzkz.profinder.core.Constants.IS_USER
import com.vzkz.profinder.core.Constants.JOBS
import com.vzkz.profinder.core.Constants.LASTNAME
import com.vzkz.profinder.core.Constants.LATITUDE
import com.vzkz.profinder.core.Constants.LOCATION
import com.vzkz.profinder.core.Constants.LOCATION_COLLECTION
import com.vzkz.profinder.core.Constants.LONGITUDE
import com.vzkz.profinder.core.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.core.Constants.NAME
import com.vzkz.profinder.core.Constants.NICKNAME
import com.vzkz.profinder.core.Constants.NONEXISTENT_SERVICEATTRIBUTE
import com.vzkz.profinder.core.Constants.OTHER_ID
import com.vzkz.profinder.core.Constants.OTHER_NICKNAME
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.PROFESSION
import com.vzkz.profinder.core.Constants.PROFILEPHOTO
import com.vzkz.profinder.core.Constants.RATING
import com.vzkz.profinder.core.Constants.REQUESTS
import com.vzkz.profinder.core.Constants.REVIEW_NUMBER
import com.vzkz.profinder.core.Constants.SERVICES_COLLECTION
import com.vzkz.profinder.core.Constants.SERVICE_ID
import com.vzkz.profinder.core.Constants.SERVICE_NAME
import com.vzkz.profinder.core.Constants.SERV_DESCRIPTION
import com.vzkz.profinder.core.Constants.STATE
import com.vzkz.profinder.core.Constants.UID
import com.vzkz.profinder.core.Constants.USERS_COLLECTION
import com.vzkz.profinder.data.dto.JobDto
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.LocationModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.roundToInt


class FirestoreService @Inject constructor(firestore: FirebaseFirestore) {
    //Users
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun nicknameExists(nickname: String): Boolean {
        val userInfo =
            usersCollection.whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: ActorModel?): Result<Unit, FirebaseError.Firestore> {
        if (userData == null) return Result.Error(FirebaseError.Firestore.USER_NOT_FOUND_IN_DATABASE)

        val userDocument = usersCollection.document(userData.uid)

        val userMap: Map<String, Any?> = userData.toMap()

        try {
            //SetOptions.merge() used to add the user without overriding other potential fields
            userDocument.set(userMap, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i("Jaime", "Success inserting user in database")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Failure inserting user: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.INSERTION_ERROR)
        }

        return Result.Success(Unit)
    }

    suspend fun getUserData(uid: String): Result<ActorModel, FirebaseError.Firestore> {

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
                    else -> return Result.Error(FirebaseError.Firestore.NON_EXISTENT_USER_FIELD)
                }
            }
            val state = if (isUser) null else {
                when (userData[STATE]) {
                    ProfState.Active.name -> ProfState.Active
                    ProfState.Working.name -> ProfState.Working
                    ProfState.Inactive.name -> ProfState.Inactive
                    else -> return Result.Error(FirebaseError.Firestore.NON_EXISTENT_USER_FIELD)
                }
            }
            val profilePicture = userData.getOrDefault(PROFILEPHOTO, null) as String?
            val reviewNumber = userData.getOrDefault(REVIEW_NUMBER, null) as Double?
            var rating = userData.getOrDefault(RATING, null) as Double?
            if (rating != null)
                rating = (rating * 100.0).roundToInt() / 100.0
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
                    rating = rating,
                    reviewNumber = reviewNumber?.toInt()
                )

                return Result.Success(userModel)
            } catch (e: Exception) {
                Log.e("Jaime", "error fetching user data from db. ${e.message}")
                return Result.Error(FirebaseError.Firestore.NON_EXISTENT_USER_FIELD)
            }
        } else return Result.Error(FirebaseError.Firestore.CONNECTION_ERROR)


    }

    suspend fun modifyUserData(
        uid: String,
        changedFields: Map<String, Any>
    ): Result<Unit, FirebaseError.Firestore> {
        if (changedFields.containsKey(NICKNAME) && nicknameExists(changedFields[NICKNAME].toString()))
            return Result.Error(FirebaseError.Firestore.NICKNAME_IN_USE)

        val userDocumentReference = usersCollection.document(uid)
        try {
            userDocumentReference.set(changedFields, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i("Jaime", "User data updated successfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error updating user data: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }
        return Result.Success(Unit)

//        if ((oldUser.nickname != newUser.nickname) && (nicknameExists(newUser.nickname))) {
//            return Result.Error(FirebaseError.Firestore.NICKNAME_IN_USE)
//        }
//
//        // Gets the reference to the user document
//        val userDocumentReference = usersCollection.document(oldUser.uid)
//
//        // Casts the new UserModel to map in order to update only the needed fields
//        val newUserMap = newUser.toMap()
//
//        // Updates firestore data and wait for the update to complete
//        try {
//            userDocumentReference.update(newUserMap)
//                .addOnSuccessListener {
//                    Log.i("Jaime", "User data updated successfully")
//                }
//                .addOnFailureListener {
//                    Log.e("Jaime", "Error updating user data: ${it.message}")
//                    throw Exception()
//                }
//        } catch (e: Exception) {
//            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
//        }
//        return Result.Success(Unit)
    }


    fun changeProfState(uid: String, state: ProfState): Result<Unit, FirebaseError.Firestore> {
        try {
            usersCollection.document(uid).update(STATE, state.name)
                .addOnSuccessListener {
                    Log.i("Jaime", "State updated successfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error updating state: ${it.message}")
                    throw Exception(MODIFICATION_ERROR)
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun changeFavouritesList(
        uidListOwner: String,
        uidToChange: String,
        add: Boolean
    ): Result<Unit, FirebaseError.Firestore> {
        val docRef = usersCollection.document(uidListOwner)
        if (add) {
            try {
                docRef.update(FAVOURITES, FieldValue.arrayUnion(uidToChange))
                    .addOnSuccessListener {
                        Log.i("Jaime", "Favourite added successfully")
                    }
                    .addOnFailureListener {
                        Log.e("Jaime", "Error adding favourite: ${it.message}")
                        throw Exception()
                    }
            } catch (e: Exception) {
                return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
            }
        } else {
            try {
                docRef.update(FAVOURITES, FieldValue.arrayRemove(uidToChange))
                    .addOnSuccessListener {
                        Log.i("Jaime", "Favourite removed successfully")
                    }
                    .addOnFailureListener {
                        Log.e("Jaime", "Error removing favourite: ${it.message}")
                        throw Exception()
                    }
            } catch (e: Exception) {
                return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
            }
        }
        return Result.Success(Unit)
    }

    suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean {
        val doc = usersCollection.document(uidListOwner).get().await()
        val favList = doc[FAVOURITES] as List<String>? ?: emptyList()
        return favList.contains(uidToCheck)
    }

    suspend fun getFavouritesList(uid: String): Result<List<ActorModel>, FirebaseError.Firestore> {
        return try {
            val doc = usersCollection.document(uid).get().await()
            val favList = doc[FAVOURITES] as List<String>?
            val favActorList = mutableListOf<ActorModel>()
            if (favList != null) {
                for (fav in favList) {
                    when (val userData = getUserData(fav)) {
                        is Result.Error -> return Result.Error(userData.error)
                        is Result.Success -> favActorList.add(userData.data)
                    }
                }
                Result.Success(favActorList.toList())
            } else {
                Result.Error(FirebaseError.Firestore.CORRUPTED_DATABASE_DATA)
            }
        } catch (e: Exception) {
            Result.Error(FirebaseError.Firestore.CORRUPTED_DATABASE_DATA)
        }
    }

    fun changeProfilePicture(uid: String, uri: Uri): Result<Unit, FirebaseError.Firestore> {
        try {
            usersCollection.document(uid).update(PROFILEPHOTO, uri)
                .addOnSuccessListener {
                    Log.i("Jaime", "Profile picture updated successfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error updating profile picture: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun updateRating(uid: String, newRating: Int): Result<Unit, FirebaseError.Firestore> {
        try {
            usersCollection.document(uid).get()
                .addOnSuccessListener { docSnapshot ->
                    val rating: Double = docSnapshot.data?.get(RATING) as Double? ?: 0.0
                    val reviewNumber: Double =
                        docSnapshot.data?.get(REVIEW_NUMBER) as Double? ?: 0.0

                    val finalRating = (rating * reviewNumber + newRating) / (reviewNumber + 1)
                    val finalReviewNumber = reviewNumber + 1

                    usersCollection.document(uid).update(RATING, finalRating)
                        .addOnSuccessListener {
                            Log.i("Jaime", "$RATING updated successfully")
                            usersCollection.document(uid).update(REVIEW_NUMBER, finalReviewNumber)
                                .addOnSuccessListener {
                                    Log.i("Jaime", "$REVIEW_NUMBER updated successfully")
                                }
                                .addOnFailureListener {
                                    Log.e("Jaime", "Error updating $REVIEW_NUMBER: ${it.message}")
                                    throw Exception()
                                }

                        }
                        .addOnFailureListener {
                            Log.e("Jaime", "Error updating $RATING: ${it.message}")
                            throw Exception()
                        }

                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error getting user for rating update: ${it.message}")
                    throw Exception()
                }
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }

    }

    suspend fun getAllUsers(): Result<List<ActorModel>, FirebaseError.Firestore> {
        return try {
            val userList: MutableList<ActorModel> = mutableListOf()
            val documents = usersCollection.get().await()
            for (userData in documents) {
                val isUser = userData[IS_USER] as Boolean
                val description: String? =
                    if (userData[DESCRIPTION] == "-") null else userData[DESCRIPTION] as String
                val profession = if (isUser) null else {
                    when (userData[PROFESSION]) {
                        Professions.Plumber.name -> Professions.Plumber
                        Professions.Hairdresser.name -> Professions.Hairdresser
                        Professions.Electrician.name -> Professions.Electrician
                        else -> return Result.Error(FirebaseError.Firestore.ERROR_GETTING_USERS)
                    }
                }
                val state = if (isUser) null else {
                    when (userData[STATE]) {
                        ProfState.Active.name -> ProfState.Active
                        ProfState.Working.name -> ProfState.Working
                        ProfState.Inactive.name -> ProfState.Inactive
                        else -> return Result.Error(FirebaseError.Firestore.ERROR_GETTING_USERS)
                    }
                }
                val profilePicture = userData.get(PROFILEPHOTO) as String?
                val reviewNumber = userData.get(REVIEW_NUMBER) as Double?
                var rating = userData.get(RATING) as Double?
                if (rating != null)
                    rating = (rating * 100.0).roundToInt() / 100.0
                val userModel = ActorModel(
                    uid = userData.id,
                    nickname = userData[NICKNAME] as String,
                    firstname = userData[FIRSTNAME] as String,
                    lastname = userData[LASTNAME] as String,
                    actor = if (isUser) Actors.User else Actors.Professional,
                    description = description,
                    profession = profession,
                    state = state,
                    profilePhoto = profilePicture?.let { Uri.parse(it) },
                    rating = rating,
                    reviewNumber = reviewNumber?.toInt()
                )

                userList.add(userModel)
            }
            Result.Success(userList.toList())
         } catch (e: Exception) {
            Result.Error(FirebaseError.Firestore.CONNECTION_ERROR)
        }
    }

    //Services
    private val servicesCollection = firestore.collection(SERVICES_COLLECTION)

    suspend fun getServiceListByUid(uid: String): Result<List<ServiceModel>, FirebaseError.Firestore> {
        val querySnapshot = servicesCollection
            .whereEqualTo(UID, uid)
            .get()
            .await()
        return fillList(querySnapshot)
    }

    suspend fun getActiveServiceList(): Result<List<ServiceModel>, FirebaseError.Firestore> {
        val querySnapshot = servicesCollection
            .whereEqualTo(IS_ACTIVE, true)
            .get()
            .await()
        return fillList(querySnapshot)
    }

    private suspend fun fillList(querySnapshot: QuerySnapshot): Result<List<ServiceModel>, FirebaseError.Firestore> {
        val serviceList = mutableListOf<ServiceModel>()
        for (document in querySnapshot.documents) {
            val category = when (document.getString(CATEGORY)) {
                Categories.Beauty.name -> Categories.Beauty
                Categories.Household.name -> Categories.Household
                else -> return Result.Error(FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE)
            }
            val ownerUid = document.getString(UID)
                ?: return Result.Error(FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE)
            when (val owner = getUserData(ownerUid)) {
                is Result.Error -> return Result.Error(owner.error)
                is Result.Success -> {
                    serviceList.add(
                        ServiceModel(
                            sid = document.id,
                            uid = ownerUid,
                            name = document.getString(NAME)
                                ?: throw Exception(NONEXISTENT_SERVICEATTRIBUTE),
                            isActive = document.getBoolean(IS_ACTIVE) ?: return Result.Error(
                                FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE
                            ),
                            category = category,
                            servDescription = document.getString(SERV_DESCRIPTION)
                                ?: return Result.Error(FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE),
                            price = document.getDouble(PRICE)
                                ?: return Result.Error(FirebaseError.Firestore.NONEXISTENT_SERVICE_ATTRIBUTE),
                            owner = owner.data
                        )
                    )
                }
            }

        }
        return Result.Success(serviceList.toList())
    }

    fun insertService(service: ServiceModel): Result<Unit, FirebaseError.Firestore> {
        val serviceDocument = servicesCollection.document()

        val serviceMap: Map<String, Any?> = service.toMap()

        try {
            //SetOptions.merge() used to add the user without overriding other potential fields
            serviceDocument.set(serviceMap, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i("Jaime", "Success inserting service in database")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Failure inserting service: ${it.message}")
                    throw it
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.INSERTION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun deleteService(sid: String): Result<Unit, FirebaseError.Firestore> {
        try {
            servicesCollection.document(sid).delete()
                .addOnSuccessListener {
                    Log.i("Jaime", "Service deleted correctly")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error deleting service: ${it.message}")
                    throw it
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.DELETION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun modifyServiceActivity(
        sid: String,
        newValue: Boolean
    ): Result<Unit, FirebaseError.Firestore> {
        try {
            servicesCollection.document(sid).update(IS_ACTIVE, newValue)
                .addOnSuccessListener {
                    Log.i("Jaime", "Service activity modified correctly")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error modifying service activity: ${it.message}")
                    throw it
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }
        return Result.Success(Unit)
    }

    //Requests/jobs
    fun getJobsOrRequests(isRequest: Boolean, uid: String): Flow<List<JobModel>> = callbackFlow {
        val requestList = mutableListOf<JobModel>()
        val collectionName = if (isRequest) REQUESTS else JOBS
        val listener =
            usersCollection.document(uid).collection(collectionName)
                .addSnapshotListener { value, error ->
                    requestList.clear()
                    value?.documents?.forEach { docSnapshot ->
                        requestList.add(
                            JobModel(
                                id = docSnapshot.id,
                                otherNickname = docSnapshot.getString(OTHER_NICKNAME)
                                    ?: throw Exception(),
                                otherUid = docSnapshot.getString(OTHER_ID) ?: throw Exception(),
                                serviceId = docSnapshot.getString(SERVICE_ID) ?: throw Exception(),
                                serviceName = docSnapshot.getString(SERVICE_NAME)
                                    ?: throw Exception(),
                                price = docSnapshot.getDouble(PRICE) ?: throw Exception(),
                                isRatingPending = docSnapshot.getBoolean(IS_RATING_PENDING) ?: false
                            )
                        )
                    }
                    if (error != null) {
                        Log.e("Jaime", "error found getting jobs/requests: ${error.message}")
                        throw Exception(error.message)
                    }
                    trySend(requestList)
                }
        awaitClose {
            // Cancel the snapshot listener when the flow is closed
            listener.remove()
        }

    }

    fun addNewJobOrRequest(
        isRequest: Boolean,
        profUid: String,
        request: JobDto
    ): Result<Unit, FirebaseError.Firestore> {
        val collectionName = if (isRequest) REQUESTS else JOBS
        val docRef = usersCollection.document(profUid).collection(collectionName).document()
        try {
            docRef.set(request.toMapProf())
                .addOnSuccessListener {
                    Log.i("Jaime", "Job/Request added correctly")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error adding job/request to firestore: ${it.message}")
                    throw Exception()
                }
            usersCollection.document(request.otherId).collection(collectionName).document(docRef.id)
                .set(request.toMapUser(profUid))
                .addOnSuccessListener {
                    Log.i("Jaime", "Job/Request added correctly")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error adding job/request to firestore: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(if (isRequest) FirebaseError.Firestore.REQUEST_ADDITION_ERROR else FirebaseError.Firestore.JOB_ADDITION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun deleteJobOrRequest(
        isRequest: Boolean,
        uid: String,
        otherUid: String,
        id: String
    ): Result<Unit, FirebaseError.Firestore> {
        try {
            val collectionName = if (isRequest) REQUESTS else JOBS
            usersCollection.document(uid).collection(collectionName).document(id).delete()
                .addOnSuccessListener {
                    Log.i("Jaime", "Job/Request deleted correctly")
                    usersCollection.document(otherUid).collection(collectionName).document(id)
                        .delete()
                        .addOnSuccessListener {
                            Log.i("Jaime", "Job/Request 2 deleted correctly")

                        }
                        .addOnFailureListener {
                            Log.e(
                                "Jaime",
                                "Error deleting job/request 2 from firestore: ${it.message}"
                            )
                            throw Exception()
                        }

                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error deleting job/request from firestore: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.DELETION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun deleteIndividualJob(
        uid: String,
        jid: String
    ): Result<Unit, FirebaseError.Firestore> {
        try {
            usersCollection.document(uid).collection(JOBS).document(jid).delete()
                .addOnSuccessListener {
                    Log.i("Jaime", "Job deleted correctly")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error deleting job from firestore: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.DELETION_ERROR)
        }
        return Result.Success(Unit)
    }

    fun turnRequestIntoJob(
        rid: String,
        uid: String,
        request: JobDto
    ): Result<Unit, FirebaseError.Firestore> {
        return when (val deletion =
            deleteJobOrRequest(isRequest = true, uid = uid, otherUid = request.otherId, id = rid)) {
            is Result.Success -> {
                when (val addition =
                    addNewJobOrRequest(isRequest = false, profUid = uid, request = request)) {
                    is Result.Error -> Result.Error(addition.error)
                    is Result.Success -> Result.Success(Unit)
                }
            }

            is Result.Error -> Result.Error(deletion.error)
        }
    }

    fun setRatingPending(uid: String, jid: String): Result<Unit, FirebaseError.Firestore> {
        return try {
            usersCollection.document(uid).collection(JOBS).document(jid)
                .update(IS_RATING_PENDING, true)
                .addOnSuccessListener {
                    Log.i("Jaime", "Rating pending updated correctly")
                }
                .addOnFailureListener { throw Exception() }
            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(FirebaseError.Firestore.MODIFICATION_ERROR)
        }
    }

    //Location
    private val locationCollection = firestore.collection(LOCATION_COLLECTION)
    fun updateUserLocation(
        uid: String,
        nickname: String,
        location: LatLng
    ): Result<Unit, FirebaseError.Firestore> {
        try {
            val locationData: MutableMap<String, Any> = mutableMapOf()
            locationData[NICKNAME] = nickname
            locationData[LOCATION] = GeoPoint(location.latitude, location.longitude)

            locationCollection.document(uid).set(locationData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i("Jaime", "Location updated succesfully")
                }
                .addOnFailureListener {
                    Log.e("Jaime", "Error updating user location: ${it.message}")
                    throw Exception()
                }
        } catch (e: Exception) {
            return Result.Error(FirebaseError.Firestore.LOCATION_UPDATE_ERROR)
        }
        return Result.Success(Unit)
    }

    fun getLocations(uid: String): Flow<List<LocationModel>> = callbackFlow {
        val locationList = mutableListOf<LocationModel>()
        val listener =
            locationCollection
                .addSnapshotListener { value, error ->
                    locationList.clear()
                    value?.documents?.forEach { docSnapshot ->
                        if (docSnapshot.id != uid) {
                            val location = docSnapshot.getGeoPoint(LOCATION)
                            val nickname = docSnapshot.getString(NICKNAME)
                            locationList.add(
                                LocationModel(
                                    uid = docSnapshot.id,
                                    nickname = nickname ?: throw Exception(),
                                    location = LatLng(
                                        location?.latitude ?: throw Exception(),
                                        location.longitude
                                    )
                                )
                            )
                        }
                    }
                    if (error != null) {
                        Log.e("Jaime", "error found getting locations: ${error.message}")
                        throw Exception(error.message)
                    }
                    trySend(locationList)
                }
        awaitClose {
            // Cancel the snapshot listener when the flow is closed
            listener.remove()
        }

    }
}

