package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.DataStoreRepository
import javax.inject.Inject


interface SaveUidDataStoreUseCase {
    suspend operator fun invoke(uid: String)
}

class SaveUidDataStoreUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) :
    SaveUidDataStoreUseCase {
    override suspend operator fun invoke(uid: String) {
        dataStoreRepository.saveUid(uid)
    }
}