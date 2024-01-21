package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel

class ServiceListSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: ServiceListSingleton? = null

        fun getServiceListInstance(repository: Repository): ServiceListSingleton {
            if (instance == null) {
                instance = ServiceListSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedServiceList: List<ServiceModel>? = null // Stores the user locally

    fun cachedServiceList(): Boolean = cachedServiceList != null

    suspend fun getData(uid: String = ""): List<ServiceModel> { //gets cached user or calls firebase
        return if (cachedServiceList == null) {
            fetchServiceListFromFirestore(uid) //get user from firestore
        } else {
            cachedServiceList!! //user cached locally
        }
    }

    fun flushCache() {
        cachedServiceList = null
    }

    private suspend fun fetchServiceListFromFirestore(uid: String): List<ServiceModel> {
        val list = repository.getServiceListFromFirestore(uid)
        cachedServiceList = list
        return list
    }

}