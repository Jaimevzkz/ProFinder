package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject

interface ChangeServiceActivityUseCase {
    suspend operator fun invoke(sid: String, actualValue: Boolean)
}


class ChangeServiceActivityUseCaseImpl @Inject constructor(
    private val repository: Repository,
): ChangeServiceActivityUseCase {
    private val instance = getServiceListInstance(repository)
    override suspend operator fun invoke(sid: String, actualValue: Boolean) {
        repository.modifyServiceActivity(sid = sid, newValue = !actualValue)
        instance.flushCache()
    }
}