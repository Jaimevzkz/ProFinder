package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserDataSingleton
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl @Inject constructor(
    private val repository: Repository
) :
    LogoutUseCase {

    private val instance = UserDataSingleton.getInstance(repository)
    override suspend operator fun invoke() {
        instance.flushCache()
        return repository.logout()
    }
}