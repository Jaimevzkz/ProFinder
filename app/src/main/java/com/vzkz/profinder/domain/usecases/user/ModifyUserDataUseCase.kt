package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject

interface ModifyUserDataUseCase {
    suspend operator fun invoke(oldUser: ActorModel, newUser: ActorModel)
}

class ModifyUserDataUseCaseImpl @Inject constructor(private val repository: Repository) :
    ModifyUserDataUseCase {
    override suspend operator fun invoke(oldUser: ActorModel, newUser: ActorModel) {
        return repository.modifyUserData(oldUser = oldUser, newUser = newUser)
    }
}