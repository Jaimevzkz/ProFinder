package com.vzkz.profinder.domain.model.singletons

import android.net.Uri
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.error.Result

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

    suspend fun getData(uid: String = ""): Result<List<ServiceModel>, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (cachedServiceList == null) {
            fetchServiceListFromFirestore(uid) //get user from firestore
        } else {
            Result.Success(cachedServiceList!!) //user cached locally
        }
    }

    fun flushCache() {
        cachedServiceList = null
    }

    fun changeServiceListOwnerProfilePic(uri: Uri) {
        cachedServiceList = cachedServiceList?.map {
            it.copy(owner = it.owner.copy(profilePhoto = uri))
        }
    }

    private suspend fun fetchServiceListFromFirestore(uid: String): Result<List<ServiceModel>, FirebaseError.Firestore> {
        return when (val list = repository.getServiceListByUidFromFirestore(uid)) {
            is Result.Success -> {
                cachedServiceList = list.data
                Result.Success(list.data)
            }

            is Result.Error -> Result.Error(list.error)
        }

    }

}