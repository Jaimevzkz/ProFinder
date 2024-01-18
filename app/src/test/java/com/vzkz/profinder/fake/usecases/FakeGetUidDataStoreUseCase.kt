package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.DataStoreRepository
import com.vzkz.profinder.domain.usecases.GetUidDataStoreUseCase
import javax.inject.Inject


class FakeGetUidDataStoreUseCase: GetUidDataStoreUseCase {
    override suspend operator fun invoke(): String {
        TODO()
    }
}