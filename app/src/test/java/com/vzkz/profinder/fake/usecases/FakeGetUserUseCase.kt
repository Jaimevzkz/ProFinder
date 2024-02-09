package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.fake.user1_test

class FakeGetUserUsecase(): GetUserUseCase {
    override suspend fun invoke(): ActorModel {
        return user1_test
    }
}