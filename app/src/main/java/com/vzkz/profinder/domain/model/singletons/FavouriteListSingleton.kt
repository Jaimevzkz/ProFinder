package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel

class FavouriteListSingleton(private val repository: Repository) {

    companion object { //Singleton
        private var instance: FavouriteListSingleton? = null

        fun getFavListInstance(repository: Repository): FavouriteListSingleton {
            if (instance == null) {
                instance = FavouriteListSingleton(repository)
            }
            return instance!!
        }
    }

    private var cachedFavList: List<ActorModel>? = null // Stores the user locally

    fun cachedList(): Boolean = cachedFavList != null

    suspend fun getData(uid: String = ""): Result<List<ActorModel>, FirebaseError.Firestore> { //gets cached user or calls firebase
        return if (cachedFavList == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            Result.Success(cachedFavList!!) //user cached locally
        }
    }

    fun flushCache() {
        cachedFavList = null
    }

    private suspend fun fetchDataFromFirestore(ownerUid: String): Result<List<ActorModel>, FirebaseError.Firestore> {
        return when(val favList = repository.getFavouriteList(ownerUid)){
            is Result.Success -> {
                cachedFavList = favList.data
                Result.Success(favList.data)
            }
            is Result.Error -> Result.Error(favList.error)
        }
    }

    fun deleteFavourite(uidToDelete: String){
        cachedFavList = cachedFavList?.filter { it.uid != uidToDelete }
    }

    suspend fun addFavourite(uidToAdd: String): Result<Unit, FirebaseError.Firestore>{
        return when(val user = repository.getUserFromFirestore(uidToAdd)){
            is Result.Success -> {
                cachedFavList = cachedFavList?.plus(user.data)
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(user.error)
        }
    }

}