package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel

class ProfileViewUserSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: ProfileViewUserSingleton? = null

        fun getUserProfileToSeeInstance(repository: Repository): ProfileViewUserSingleton {
            if (instance == null) {
                instance = ProfileViewUserSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedUser: ActorModel? = null // Stores the user locally

    fun cachedUser(uid: String): Boolean = cachedUser != null && cachedUser?.uid == uid

    suspend fun getData(uid: String = ""): Result<ActorModel, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (uid != "") {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            Result.Success(cachedUser!!) //user cached locally
        }
    }

    fun setUserData(user: ActorModel) {
        cachedUser = user
    }

    fun flushCache() {
        cachedUser = null
    }

    private suspend fun fetchDataFromFirestore(uid: String): Result<ActorModel, FirebaseError.Firestore> {
        return when (val user = repository.getUserFromFirestore(uid)) {
            is Result.Success -> {
                cachedUser = user.data
                Result.Success(user.data)
            }

            is Result.Error -> Result.Error(user.error)
        }
    }

}