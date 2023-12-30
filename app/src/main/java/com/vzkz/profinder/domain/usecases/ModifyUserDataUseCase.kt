package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

interface ModifyUserDataUseCase {
    suspend operator fun invoke(oldUser: UserModel, newUser: UserModel)
}

class ModifyUserDataUseCaseImpl @Inject constructor(private val repository: Repository) :
    ModifyUserDataUseCase {
    override suspend operator fun invoke(oldUser: UserModel, newUser: UserModel) {
        return repository.modifyUserData(oldUser = oldUser, newUser = newUser)
    }
}