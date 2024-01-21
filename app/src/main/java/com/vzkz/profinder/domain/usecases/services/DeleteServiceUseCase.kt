package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject

interface DeleteServiceUseCase {
    operator fun invoke(sid: String)
}


class DeleteServiceUseCaseImpl @Inject constructor(
    private val repository: Repository
): DeleteServiceUseCase {

    private val instance = getServiceListInstance(repository)
    override operator fun invoke(sid: String) {
        repository.deleteService(sid)
        instance.flushCache()
    }
}