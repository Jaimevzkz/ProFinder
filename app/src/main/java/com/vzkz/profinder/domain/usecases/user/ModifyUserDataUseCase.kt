package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject
import com.vzkz.profinder.domain.error.Result

interface ModifyUserDataUseCase {
    suspend operator fun invoke(
        oldUser: ActorModel,
        newUser: ActorModel
    ): Result<Unit, FirebaseError.Firestore>
}

class ModifyUserDataUseCaseImpl @Inject constructor(private val repository: Repository) :
    ModifyUserDataUseCase {
    override suspend operator fun invoke(
        oldUser: ActorModel,
        newUser: ActorModel
    ): Result<Unit, FirebaseError.Firestore> =
        repository.modifyUserData(oldUser = oldUser, newUser = newUser)
}