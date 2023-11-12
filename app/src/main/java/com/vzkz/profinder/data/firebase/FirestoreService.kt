package com.vzkz.profinder.data.firebase

import android.content.res.Resources
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.vzkz.profinder.R
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

const val USERS_COLLECTION = "users"
const val UID = "uid"

class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {
    fun userExists(nickname: String): Boolean{
        var exists = false

        firestore.collection(USERS_COLLECTION).document(nickname).get()
            .addOnSuccessListener { document ->
                exists = document != null
            }
            .addOnFailureListener { exception ->
                Log.e("Jaime", "error getting doc. ${exception.message}")
                throw Exception(Resources.getSystem().getString(R.string.network_failure_while_checking_user_existence))
            }
        return exists
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

    fun getUserData(uid: String?): String{ //returns nickname
        var nickname = ""
        firestore.collection(USERS_COLLECTION).whereEqualTo(UID, uid).get().addOnSuccessListener{ documents ->
            documents.forEach{//Size will always be 1 (uid is unique)
                nickname = it.id
            }
        }.addOnFailureListener {
            throw Exception(Resources.getSystem().getString(R.string.user_not_found)) //This exception should never be thrown
        }
        return nickname
    }

}
