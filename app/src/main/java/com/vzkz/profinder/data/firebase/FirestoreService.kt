package com.vzkz.profinder.data.firebase

import android.content.res.Resources
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.vzkz.profinder.R
import com.vzkz.profinder.data.firebase.Constants.UID
import com.vzkz.profinder.data.firebase.Constants.USERS_COLLECTION
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private object  Constants{
    const val USERS_COLLECTION = "users"
    const val UID = "uid"
}


class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun userExists(nickname: String): Boolean { //TO test
        try {
            val documentSnapshot = firestore.collection("users")
                .document(nickname)
                .get()
                .await()

            return documentSnapshot.exists()
        } catch (e: Exception) {
            Log.e("Jaime", "error getting doc. ${e.message}")
            throw Exception("Network Failure while checking user existence")
        }
    }

    fun insertUser(userData: UserModel?){
        if (userData == null) throw Exception(Resources.getSystem().getString(R.string.couldn_t_insert_user_in_database))
        val user = hashMapOf(
            UID to userData.uid
        )
        //At this point, we know for a fact that nickname is unique
        firestore.collection(USERS_COLLECTION).document(userData.nickname).set(user).addOnSuccessListener {
            Log.i("Jaime", "Success inserting in database")
        }
        .addOnFailureListener {
            Log.e("Jaime", "Failure ${it.message}")
        }

    }

    suspend fun getUserData(uid: String): String { //returns nickname //TO test
        var nickname = ""
        val source = Source.DEFAULT
        val userInfo = firestore.collection(USERS_COLLECTION).whereEqualTo(UID, uid).get(source).await()
        if(userInfo.isEmpty) throw Exception("User not found") //This exception should never be thrown
        userInfo.forEach{
            nickname = it.id
        }
        return nickname
    }

}
