package com.vzkz.profinder.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.vzkz.profinder.domain.model.Constants.CONNECTION_ERROR
import com.vzkz.profinder.domain.model.Constants.MODIFICATION_ERROR
import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.NICKNAME_IN_USE
import com.vzkz.profinder.domain.model.Constants.NULL_USERDATA
import com.vzkz.profinder.domain.model.Constants.UID
import com.vzkz.profinder.domain.model.Constants.UNKNOWN_EXCEPTION
import com.vzkz.profinder.domain.model.Constants.USERS_COLLECTION
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {

    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun nicknameExists(nickname: String): Boolean {
        val userInfo =
            firestore.collection(USERS_COLLECTION).whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: UserModel?) {
        if (userData == null) throw Exception(NULL_USERDATA)

        val userDocument = usersCollection.document(userData.uid)

        val user: Map<String, Any?> = userData.toMap()


        // We use SetOptions.merge() to add the user without overriding other potential fields
        userDocument.set(user, SetOptions.merge())
            .addOnSuccessListener {
                Log.i("Jaime", "Success inserting in database")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Failure ${it.message}")
            }

    }

    suspend fun getUserData(uid: String): UserModel {
        return try {
            val userDocumentRef = usersCollection.document(uid)

            val userDoc = userDocumentRef.get().await()


            val userData = userDoc.data

            // Asegurarse de que userData no sea nulo
            if (userData != null) {

                val userModel = UserModel(
                    uid = userData[UID] as String,
                    nickname = userData[NICKNAME] as String,
                )

                userModel
            } else throw Exception(CONNECTION_ERROR)

        } catch (e: Exception) {
            Log.e("Jaime", "error getting doc. ${e.message}")
            when (e.message) {
                CONNECTION_ERROR -> throw Exception(CONNECTION_ERROR)
                else -> throw Exception(UNKNOWN_EXCEPTION)
            }
        }
    }

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel) {
        if ((oldUser.nickname != newUser.nickname) && (nicknameExists(newUser.nickname))) {
            throw Exception(NICKNAME_IN_USE)
        }

        // Gets the reference to the user document
        val userDocumentReference = usersCollection.document(oldUser.uid)

        // Casts the new UserModel to map in order to update only the needed fields
        val newUserMap = newUser.toMap()

        // Updates firestore data and wait for the update to complete
        userDocumentReference.update(newUserMap).addOnSuccessListener {
            Log.i("Jaime", "User data updated successfully")
        }
            .addOnFailureListener {
                Log.e("Jaime", "Error updating user data")
                throw Exception(MODIFICATION_ERROR)
            }

    }

}
