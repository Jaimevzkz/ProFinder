package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject
import com.vzkz.profinder.domain.error.Result


interface ChangeStateUseCase {
    suspend operator fun invoke(
        uid: String,
        state: ProfState
    ): Result<Unit, FirebaseError.Firestore>
}

class ChangeStateUseCaseImpl @Inject constructor(private val repository: Repository) :
    ChangeStateUseCase {
    private val instance = UserDataSingleton.getUserInstance(repository)
    override suspend operator fun invoke(
        uid: String,
        state: ProfState
    ): Result<Unit, FirebaseError.Firestore> {
        instance.flushCache()
        return repository.changeProfState(uid, state)
    }
}