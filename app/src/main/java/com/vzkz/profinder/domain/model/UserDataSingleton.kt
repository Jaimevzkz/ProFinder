package com.vzkz.profinder.domain.model

import com.vzkz.profinder.domain.Repository

class UserDataSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: UserDataSingleton? = null

        fun getInstance(repository: Repository): UserDataSingleton {
            if (instance == null) {
                instance = UserDataSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedUser: UserModel? = null // Stores the user locally

    fun cachedUser(): Boolean = cachedUser != null

    suspend fun getData(uid: String = ""): UserModel { //gets cached user or calls firebase
        return if (cachedUser == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            cachedUser!! //user cached locally
        }
    }

    private suspend fun fetchDataFromFirestore(uid: String): UserModel {
        return repository.getUserFromFirestore(uid)
    }

}