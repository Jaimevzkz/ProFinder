package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ServiceModel

class ActiveServiceListSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: ActiveServiceListSingleton? = null

        fun getActiveServiceListInstance(repository: Repository): ActiveServiceListSingleton {
            if (instance == null) {
                instance = ActiveServiceListSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedServiceList: List<ServiceModel>? = null // Stores the user locally

    suspend fun getData(): Result<List<ServiceModel>, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (cachedServiceList == null) {
            fetchServiceListFromFirestore() //get user from firestore
        } else {
            Result.Success(cachedServiceList!!) //user cached locally
        }
    }

    fun flushCache() {
        cachedServiceList = null
    }

    private suspend fun fetchServiceListFromFirestore(): Result<List<ServiceModel>, FirebaseError.Firestore> {
        return when (val list = repository.getActiveServiceListFromFirestore()) {
            is Result.Success -> {
                cachedServiceList = list.data
                Result.Success(list.data)
            }

            is Result.Error -> Result.Error(list.error)
        }

    }

}