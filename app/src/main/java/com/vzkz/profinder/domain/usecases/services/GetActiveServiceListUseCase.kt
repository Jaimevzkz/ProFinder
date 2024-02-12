package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ActiveServiceListSingleton.Companion.getActiveServiceListInstance
import javax.inject.Inject

interface GetActiveServiceListUseCase {
    suspend operator fun invoke(): List<ServiceModel>
}


class GetActiveServiceListUseCaseImpl @Inject constructor(
    repository: Repository
): GetActiveServiceListUseCase {

    private val instance = getActiveServiceListInstance(repository)
    override suspend operator fun invoke(): List<ServiceModel> {
        return instance.getData()
    }
}