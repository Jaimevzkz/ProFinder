package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.usecases.auth.SignUpUseCase


class FakeSignUpUseCase: SignUpUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel> {
        TODO("Not yet implemented")
    }
}