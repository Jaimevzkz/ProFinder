package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ThemeDSUseCase {
    suspend fun switchTheme()
    suspend operator fun invoke(): Flow<Boolean>
}

class ThemeDSUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) :
    ThemeDSUseCase {
    override suspend fun switchTheme() {
        dataStoreRepository.switchAppTheme()
    }

    override suspend operator fun invoke(): Flow<Boolean> {
        return dataStoreRepository.getAppTheme()
    }
}