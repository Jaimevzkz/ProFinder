package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.usecases.SaveUidDataStoreUseCase


class FakeSaveUidDataStoreUseCase : SaveUidDataStoreUseCase {
    override suspend operator fun invoke(uid: String) {
        TODO()
    }
}