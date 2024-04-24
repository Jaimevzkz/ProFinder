package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import javax.inject.Inject

interface ChangeServiceActivityUseCase {
    suspend operator fun invoke(sid: String, actualValue: Boolean): Result<Unit, FirebaseError.Firestore>
}

class ChangeServiceActivityUseCaseImpl @Inject constructor(
    private val repository: Repository,
): ChangeServiceActivityUseCase {
    private val instance = getServiceListInstance(repository)
    override suspend operator fun invoke(sid: String, actualValue: Boolean): Result<Unit, FirebaseError.Firestore> {
        instance.flushCache()
        return repository.modifyServiceActivity(sid = sid, newValue = !actualValue)
    }
}