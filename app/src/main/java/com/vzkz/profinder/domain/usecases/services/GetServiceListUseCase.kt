package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject

interface GetServiceListUseCase {
    suspend operator fun invoke(): Result<Pair<List<ServiceModel>, List<ServiceModel>>, FirebaseError.Firestore>

}

class GetServiceListUseCaseImpl @Inject constructor(
    repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetServiceListUseCase {

    private val instance = getServiceListInstance(repository)
    override suspend operator fun invoke(): Result<Pair<List<ServiceModel>, List<ServiceModel>>, FirebaseError.Firestore> { //Pair.first -> Active services
        return if (!instance.cachedServiceList()) {
            when (val serviceList = instance.getData(getUidDataStoreUseCase())) {
                is Result.Success -> Result.Success(serviceList.data.partition { it.isActive })
                is Result.Error -> Result.Error(serviceList.error)
            }
        } else {
            when (val serviceList = instance.getData()) {
                is Result.Success -> Result.Success(serviceList.data.partition { it.isActive })
                is Result.Error -> Result.Error(serviceList.error)
            }
        }
    }
}