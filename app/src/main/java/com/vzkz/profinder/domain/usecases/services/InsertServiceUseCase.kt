package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject

interface InsertServiceUseCase {
    suspend operator fun invoke(service: ServiceModel): Result<Unit, FirebaseError.Firestore>}


class InsertServiceUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): InsertServiceUseCase {
    private val instance = getServiceListInstance(repository)
    override suspend operator fun invoke(service: ServiceModel): Result<Unit, FirebaseError.Firestore> {
        val uid = getUidDataStoreUseCase()
        instance.flushCache()
        return repository.insertServiceInFirestore(service.copy(uid = uid))
    }
}