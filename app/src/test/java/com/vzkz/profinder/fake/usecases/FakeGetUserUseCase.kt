package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.UserModel
import com.vzkz.profinder.domain.usecases.GetUserUseCase

class FakeGetUserUsecase(): GetUserUseCase {
    override suspend fun invoke(): UserModel {
        TODO("Not yet implemented")
    }
}