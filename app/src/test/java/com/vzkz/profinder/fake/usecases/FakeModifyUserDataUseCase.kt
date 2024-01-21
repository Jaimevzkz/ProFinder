package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.user.ModifyUserDataUseCase

class FakeModifyUserDataUseCase(val succes: Boolean): ModifyUserDataUseCase {
    override suspend fun invoke(oldUser: ActorModel, newUser: ActorModel) {
        if(!succes)
            RuntimeException("boom...")

    }
}