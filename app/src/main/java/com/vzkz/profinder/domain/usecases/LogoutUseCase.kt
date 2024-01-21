package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl @Inject constructor(
    private val repository: Repository
) :
    LogoutUseCase {

    private val userInstance = UserDataSingleton.getUserInstance(repository)
    private val serviceListInstance = ServiceListSingleton.getServiceListInstance(repository)
    override suspend operator fun invoke() {
        userInstance.flushCache()
        serviceListInstance.flushCache()
        return repository.logout()
    }
}