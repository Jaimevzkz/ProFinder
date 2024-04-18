package com.vzkz.profinder.domain.model.singletons

import android.net.Uri
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel

class UserDataSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: UserDataSingleton? = null

        fun getUserInstance(repository: Repository): UserDataSingleton {
            if (instance == null) {
                instance = UserDataSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedUser: ActorModel? = null // Stores the user locally

    fun cachedUser(): Boolean = cachedUser != null

    suspend fun getData(uid: String = ""): Result<ActorModel, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (cachedUser == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            Result.Success(cachedUser!!) //user cached locally
        }
    }

    fun setProfilePhoto(uri: Uri) {
        cachedUser = cachedUser?.copy(profilePhoto = uri)
    }

    fun flushCache() {
        cachedUser = null
    }

    private suspend fun fetchDataFromFirestore(uid: String): Result<ActorModel, FirebaseError.Firestore> {
        return when(val user = repository.getUserFromFirestore(uid)){
            is Result.Success -> {
                cachedUser = user.data
                Result.Success(user.data)
            }
            is Result.Error -> Result.Error(user.error)
        }
    }

}