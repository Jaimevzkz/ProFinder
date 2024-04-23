package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface ModifyUserDataUseCase {
    suspend operator fun invoke(
        uid: String,
        changedFields: Map<String, Any>
    ): Result<Unit, FirebaseError.Firestore>
}

class ModifyUserDataUseCaseImpl @Inject constructor(private val repository: Repository) :
    ModifyUserDataUseCase {

    private val instance = UserDataSingleton.getUserInstance(repository)
    override suspend operator fun invoke(
        uid: String,
        changedFields: Map<String, Any>
    ): Result<Unit, FirebaseError.Firestore> {
        instance.flushCache()
        return repository.modifyUserData(uid = uid, changedFields = changedFields)
    }
}