package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class ModifyUserDataUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(oldUser: UserModel, newUser: UserModel) {
        return repository.modifyUserData(oldUser, newUser)
    }
}