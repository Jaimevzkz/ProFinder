package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCase


class FakeSaveUidDataStoreUseCase : SaveUidDataStoreUseCase {
    override suspend operator fun invoke(uid: String) {
        //do nothing
    }
}