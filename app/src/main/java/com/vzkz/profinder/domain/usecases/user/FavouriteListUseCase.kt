package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.singletons.FavouriteListSingleton
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface FavouriteListUseCase {
    suspend fun changeFavouriteList(uidToChange: String, add: Boolean)
    suspend fun checkIsFavourite(uidToCheck: String): Boolean
    suspend fun getFavouriteList(): List<ActorModel>
}

class FavouriteListUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository
) :
    FavouriteListUseCase {
    private val favListInstance = FavouriteListSingleton.getFavListInstance(repository)
    override suspend fun changeFavouriteList(uidToChange: String, add: Boolean) {
        val ownerUid = dataStoreRepository.getUid()
        repository.changeFavouriteList(
            uidListOwner = ownerUid,
            uidToChange = uidToChange,
            add = add
        )
        if (add) {
            favListInstance.addFavourite(uidToChange)
        } else{
            favListInstance.deleteFavourite(uidToChange)
        }
    }

    override suspend fun checkIsFavourite(uidToCheck: String): Boolean {
        val ownerUid = dataStoreRepository.getUid()
        return repository.checkIsFavourite(
            uidListOwner = ownerUid,
            uidToCheck = uidToCheck
        )
    }
    override suspend fun getFavouriteList(): List<ActorModel>{
        return if (!favListInstance.cachedList()) {
            favListInstance.getData(dataStoreRepository.getUid())
        } else {
            favListInstance.getData()
        }
    }
}