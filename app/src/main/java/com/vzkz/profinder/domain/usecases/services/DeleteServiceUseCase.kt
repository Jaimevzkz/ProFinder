package com.vzkz.profinder.domain.usecases.services

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton.Companion.getServiceListInstance
import javax.inject.Inject

interface DeleteServiceUseCase {
    operator fun invoke(sid: String): Result<Unit, FirebaseError.Firestore>
}

class DeleteServiceUseCaseImpl @Inject constructor(
    private val repository: Repository
) : DeleteServiceUseCase {

    private val instance = getServiceListInstance(repository)
    override operator fun invoke(sid: String): Result<Unit, FirebaseError.Firestore> {
        instance.flushCache()
        return repository.deleteService(sid)
    }
}