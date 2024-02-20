package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton.Companion.getUserInstance
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject

interface GetUserUseCase {
    suspend operator fun invoke(): ActorModel
}


class GetUserUseCaseImpl @Inject constructor(
    repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetUserUseCase {
    private val instance = getUserInstance(repository)
    override suspend operator fun invoke(): ActorModel {
        return if (!instance.cachedUser()) {
            instance.getData(getUidDataStoreUseCase())
        } else instance.getData()
    }
}