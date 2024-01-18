package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserDataSingleton.Companion.getInstance
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject

interface GetUserUseCase {
    suspend operator fun invoke(): ActorModel
}


class GetUserUseCaseImpl @Inject constructor(
    repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
):  GetUserUseCase{ //TODO Throws Exception
    private val instance = getInstance(repository)
    override suspend operator fun invoke(): ActorModel {
        return if (!instance.cachedUser()) {
            instance.getData(getUidDataStoreUseCase())
        } else instance.getData()
    }
}