package com.vzkz.profinder.domain.model.singletons

import com.vzkz.profinder.domain.Repository
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

    suspend fun getData(uid: String = ""): List<ActorModel> { //gets cached user or calls firebase
        return if (cachedFavList == null) {
            fetchDataFromFirestore(uid) //get user from firestore
        } else {
            cachedFavList!! //user cached locally
        }
    }

    fun flushCache() {
        cachedFavList = null
    }

    private suspend fun fetchDataFromFirestore(ownerUid: String): List<ActorModel> {
        val favList = repository.getFavouriteList(ownerUid)
        cachedFavList = favList
        return favList
    }

    fun deleteFavourite(uidToDelete: String){
        cachedFavList = cachedFavList?.filter { it.uid != uidToDelete }
    }

    suspend fun addFavourite(uidToAdd: String){
        val user = repository.getUserFromFirestore(uidToAdd)
        cachedFavList = cachedFavList?.plus(user)
    }

}