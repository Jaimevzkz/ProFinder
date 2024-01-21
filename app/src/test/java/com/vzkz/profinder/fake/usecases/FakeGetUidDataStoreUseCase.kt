package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase


class FakeGetUidDataStoreUseCase: GetUidDataStoreUseCase {
    override suspend operator fun invoke(): String {
        TODO()
    }
}