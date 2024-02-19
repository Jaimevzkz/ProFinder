package com.vzkz.profinder.data.firebase

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Constants.CONNECTION_ERROR
import com.vzkz.profinder.domain.model.Constants.DESCRIPTION
import com.vzkz.profinder.domain.model.Constants.FIRSTNAME
import com.vzkz.profinder.domain.model.Constants.IS_USER
import com.vzkz.profinder.domain.model.Constants.LASTNAME
import com.vzkz.profinder.domain.model.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.NICKNAME_IN_USE
import com.vzkz.profinder.domain.model.Constants.NULL_USERDATA
import com.vzkz.profinder.domain.model.Constants.STATE
import com.vzkz.profinder.domain.model.Constants.UNKNOWN_EXCEPTION
import com.vzkz.profinder.domain.model.Constants.USERS_COLLECTION
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.domain.model.Constants.CATEGORY
import com.vzkz.profinder.domain.model.Constants.ERRORSTR
import com.vzkz.profinder.domain.model.Constants.FAVOURITES
import com.vzkz.profinder.domain.model.Constants.IS_ACTIVE
import com.vzkz.profinder.domain.model.Constants.NAME
import com.vzkz.profinder.domain.model.Constants.PRICE
import com.vzkz.profinder.domain.model.Constants.SERVICES_COLLECTION
import com.vzkz.profinder.domain.model.Constants.SERV_DESCRIPTION
import com.vzkz.profinder.domain.model.Constants.UID
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {
    //Users
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun nicknameExists(nickname: String): Boolean {
        val userInfo =
            firestore.collection(USERS_COLLECTION).whereEqualTo(NICKNAME, nickname).get().await()
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
            }

    }

    suspend fun getUserData(uid: String): ActorModel {
        return try {
            val userDocumentRef = usersCollection.document(uid)

            val userDoc = userDocumentRef.get().await()

            val userData = userDoc.data

            if (userData != null) {
                val isUser = userData[IS_USER] as Boolean
                val description = if (userData[DESCRIPTION] == "-") null else userData[DESCRIPTION]
                val profession = if (isUser) null else {
                    when (userData[DESCRIPTION]) {
                        Professions.Plumber.name -> Professions.Plumber
                        Professions.Hairdresser.name -> Professions.Hairdresser
                        Professions.Electrician.name -> Professions.Electrician
                        else -> Professions.Plumber //This should never happen
                    }
                }
                val state = if (isUser) null else {
                    when (userData[STATE]) {
                        ProfState.Active.name -> ProfState.Active
                        ProfState.Working.name -> ProfState.Working
                        ProfState.Inactive.name -> ProfState.Inactive
                        else -> ProfState.Active //This should never happen
                    }
                }
                val userModel = ActorModel(
                    uid = uid,
                    nickname = userData[NICKNAME] as String,
                    firstname = userData[FIRSTNAME] as String,
                    lastname = userData[LASTNAME] as String,
                    actor = if (isUser) Actors.User else Actors.Professional,
                    description = description as String?,
                    profession = profession,
                    state = state
                )
                userModel
            } else throw Exception(CONNECTION_ERROR)

        } catch (e: Exception) {
            Log.e("Jaime", "error getting user doc. ${e.message}")
            when (e.message) {
                CONNECTION_ERROR -> throw Exception(CONNECTION_ERROR)
                else -> throw Exception(UNKNOWN_EXCEPTION)
            }
        }
    }

    suspend fun modifyUserData(oldUser: ActorModel, newUser: ActorModel) { //todo this functionality does not work
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

    fun changeProfState(uid: String, state: ProfState){
        usersCollection.document(uid).update(STATE, state.name)
            .addOnSuccessListener {
                Log.i("Jaime", "State updated successfully")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating state: ${it.message}")
                throw Exception(MODIFICATION_ERROR)
            }
    }

    fun changeFavouritesList(uidListOwner: String, uidToChange: String, add: Boolean){
        val docRef = usersCollection.document(uidListOwner)
        if(add){
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

    suspend fun checkIsFavourite(uidListOwner: String, uidToCheck: String): Boolean{
        val doc = usersCollection.document(uidListOwner).get().await()
        val favList = doc[FAVOURITES] as List<String>
        return favList.contains(uidToCheck)
    }

    suspend fun getFavouritesList(uid: String): List<ActorModel>{
        val doc = usersCollection.document(uid).get().await()
        val favList = doc[FAVOURITES] as List<String>
        val favActorList = mutableListOf<ActorModel>()
        for(fav in favList){
            favActorList.add(getUserData(fav))
        }
        return favActorList.toList()
    }

    //Services
    private val servicesCollection = firestore.collection(SERVICES_COLLECTION)

    suspend fun getServiceListByUid(uid: String): List<ServiceModel>{
        try{
            val querySnapshot = servicesCollection
                .whereEqualTo(UID, uid)
                .get()
                .await()
            return fillList(querySnapshot)
        } catch(e: Exception){
            Log.e("Jaime", "error getting services doc. ${e.message}")
            throw Exception(UNKNOWN_EXCEPTION)
        }
    }

    suspend fun getActiveServiceList(): List<ServiceModel>{
        try{
            val querySnapshot = servicesCollection
                .whereEqualTo(IS_ACTIVE, true)
                .get()
                .await()
            return fillList(querySnapshot)
        } catch(e: Exception){
            Log.e("Jaime", "error getting services doc. ${e.message}")
            throw Exception(UNKNOWN_EXCEPTION)
        }
    }

    private suspend fun fillList(querySnapshot: QuerySnapshot): List<ServiceModel> {
        val serviceList = mutableListOf<ServiceModel>()
        for(document in querySnapshot.documents){
            val category = when(document.getString(CATEGORY)){
                Categories.Beauty.name -> Categories.Beauty
                Categories.Household.name -> Categories.Household
                else -> Categories.Beauty
            }
            val ownerUid = document.getString(UID) ?: ERRORSTR
            val owner = getUserData(ownerUid)
            serviceList.add(
                ServiceModel(
                    sid = document.id,
                    uid = ownerUid,
                    name = document.getString(NAME) ?: ERRORSTR,
                    isActive = document.getBoolean(IS_ACTIVE) ?: false,
                    category = category,
                    servDescription = document.getString(SERV_DESCRIPTION) ?: ERRORSTR,
                    price = document.getLong(PRICE)?.toDouble() ?: 0.0,
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
                throw Exception()
            }
    }

    fun deleteService(sid: String){
        servicesCollection.document(sid).delete()
            .addOnSuccessListener{
                Log.i("Jaime", "Service deleted correctly")
            }
            .addOnFailureListener{
                Log.e("Jaime", "Error deleting service: ${it.message}")
                throw Exception()
            }
    }

    fun modifyServiceActivity(sid: String, newValue: Boolean){
        servicesCollection.document(sid).update(IS_ACTIVE, newValue)
            .addOnSuccessListener{
                Log.i("Jaime", "Service activity modified correctly")
            }
            .addOnFailureListener{
                Log.e("Jaime", "Error modifying service activity: ${it.message}")
                throw Exception()
            }
    }

    suspend fun getService(sid: String): ServiceModel {
        return try {
            val servicesDocumentRef = servicesCollection.document(sid)

            val servicesDoc = servicesDocumentRef.get().await()

            val serviceData = servicesDoc.data

            if (serviceData != null) {
                val servCat = when(serviceData[CATEGORY]){
                    Categories.Beauty.name -> Categories.Beauty
                    Categories.Household.name -> Categories.Household
                    else -> Categories.Beauty //<- Error
                }
                val uid = serviceData[UID] as String
                val owner = getUserData(uid)
                val serviceModel = ServiceModel(
                    sid = sid,
                    uid = serviceData[UID] as String,
                    name = serviceData[NAME] as String,
                    isActive = serviceData[IS_ACTIVE] as Boolean,
                    category = servCat,
                    servDescription = serviceData[SERV_DESCRIPTION] as String,
                    owner = owner,
                    price = serviceData[PRICE] as Double
                )
                serviceModel
            } else throw Exception(CONNECTION_ERROR)

        } catch (e: Exception) {
            Log.e("Jaime", "error getting services doc. ${e.message}")
            when (e.message) {
                CONNECTION_ERROR -> throw Exception(CONNECTION_ERROR)
                else -> throw Exception(UNKNOWN_EXCEPTION)
            }
        }
    }

    suspend fun modifyService(oldService: ServiceModel, newService: ServiceModel) {
        // Gets the reference to the user document
        val serviceDocumentReference = servicesCollection.document(oldService.sid)

        // Casts the new UserModel to map in order to update only the needed fields
        val newServiceMap = newService.toMap()

        // Updates firestore data and wait for the update to complete
        serviceDocumentReference.update(newServiceMap)
            .addOnSuccessListener {
                Log.i("Jaime", "Service data updated successfully")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating service data")
                throw Exception(MODIFICATION_ERROR)
            }
    }


}
