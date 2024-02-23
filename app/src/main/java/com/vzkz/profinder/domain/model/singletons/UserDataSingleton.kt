package com.vzkz.profinder.domain.model.singletons

import android.net.Uri
import com.vzkz.profinder.domain.Repository
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

    suspend fun getData(uid: String = ""): ActorModel { //gets cached user or calls firebase
        return if (cachedUser == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            cachedUser!! //user cached locally
        }
    }

    fun setProfilePhoto(uri: Uri) {
        cachedUser = cachedUser?.copy(profilePhoto = uri)
    }

    fun flushCache() {
        cachedUser = null
    }

    private suspend fun fetchDataFromFirestore(uid: String): ActorModel {
        val user = repository.getUserFromFirestore(uid)
        cachedUser = user
        return user
    }

}