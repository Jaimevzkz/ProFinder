package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataStoreUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(): UserModel {
        return dataStoreRepository.getUser()
    }
}