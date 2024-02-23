package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
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

    suspend fun getData(): List<ServiceModel> { //gets cached user or calls firebase
        return if (cachedServiceList == null) {
            fetchServiceListFromFirestore() //get user from firestore
        } else {
            cachedServiceList!! //user cached locally
        }
    }

    fun flushCache() {
        cachedServiceList = null
    }

    private suspend fun fetchServiceListFromFirestore(): List<ServiceModel> {
        val list = repository.getActiveServiceListFromFirestore()
        cachedServiceList = list
        return list
    }

}