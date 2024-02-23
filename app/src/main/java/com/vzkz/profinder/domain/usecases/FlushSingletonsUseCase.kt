package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.singletons.ActiveServiceListSingleton
import com.vzkz.profinder.domain.model.singletons.FavouriteListSingleton
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface FlushSingletonsUseCase {
    suspend operator fun invoke()
}

class FlushSingletonsUseCaseImpl @Inject constructor(
    repository: Repository
) : FlushSingletonsUseCase {

    private val activeServiceListInstance = ActiveServiceListSingleton.getActiveServiceListInstance(repository)
    private val favouriteServiceListInstance = FavouriteListSingleton.getFavListInstance(repository)
    private val profileViewUserSingleton = UserDataSingleton.getUserInstance(repository)
    private val serviceListInstance = ServiceListSingleton.getServiceListInstance(repository)
    private val userInstance = UserDataSingleton.getUserInstance(repository)
    override suspend operator fun invoke() {
        activeServiceListInstance.flushCache()
        favouriteServiceListInstance.flushCache()
        profileViewUserSingleton.flushCache()
        serviceListInstance.flushCache()
        userInstance.flushCache()
    }
}