package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel

class UserListSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: UserListSingleton? = null

        fun getUserListInstance(repository: Repository): UserListSingleton {
            if (instance == null) {
                instance = UserListSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedUserList: List<ActorModel>? = null

    fun cachedServiceList(): Boolean = cachedUserList != null

    suspend fun getData(): Result<List<ActorModel>, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (cachedUserList == null) {
            fetchAllUsersFromFirestore()
        } else {
            Result.Success(cachedUserList!!)
        }
    }

    fun flushCache() {
        cachedUserList = null
    }


    private suspend fun fetchAllUsersFromFirestore(): Result<List<ActorModel>, FirebaseError.Firestore> {
        return when (val list = repository.getAllUsers()) {
            is Result.Success -> {
                cachedUserList = list.data
                Result.Success(list.data)
            }

            is Result.Error -> Result.Error(list.error)
        }

    }

}