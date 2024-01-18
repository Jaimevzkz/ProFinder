package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.SignUpUseCase


class FakeSignUpUseCase: SignUpUseCase {
    override suspend operator fun invoke(email: String, password: String, nickname: String): ActorModel? {
        TODO()
    }
}