package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject

interface GetServiceListUseCase {
    suspend operator fun invoke(): Pair<List<ServiceModel>, List<ServiceModel>>
}


class GetServiceListUseCaseImpl @Inject constructor(
    repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetServiceListUseCase { //TODO Throws Exception when firestore call fails

    private val instance = getServiceListInstance(repository)
    override suspend operator fun invoke(): Pair<List<ServiceModel>, List<ServiceModel>> { //Pair.first -> Active services
        return if (!instance.cachedServiceList()) {
            instance.getData(getUidDataStoreUseCase()).partition { it.isActive }
        }
        else instance.getData().partition { it.isActive }
    }
}