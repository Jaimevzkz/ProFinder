package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class SaveUserDataStoreUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(user: UserModel) {
        dataStoreRepository.saveUser(user)
    }
}