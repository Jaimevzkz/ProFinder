package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import javax.inject.Inject

interface GetUidDataStoreUseCase {
    suspend operator fun invoke(): String
}

class GetUidDataStoreUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): GetUidDataStoreUseCase {
    override suspend operator fun invoke(): String {
        return dataStoreRepository.getUid()
    }
}