package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import javax.inject.Inject

class SaveUidDataStoreUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(uid: String) {
        dataStoreRepository.saveUid(uid)
    }
}