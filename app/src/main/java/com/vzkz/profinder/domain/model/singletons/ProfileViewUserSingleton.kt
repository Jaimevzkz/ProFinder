package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
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

    fun cachedUser(): Boolean = cachedUser != null

    suspend fun getData(uid: String = ""): ActorModel { //gets cached user or calls firebase
        return if (cachedUser == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            cachedUser!! //user cached locally
        }
    }

    fun setUserData(user: ActorModel) {
        cachedUser = user
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