package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.LoginUseCase
import com.vzkz.profinder.fake.user1_test
import javax.inject.Inject


class FakeLoginUseCase @Inject constructor(
    private val isSuccessful: Boolean
): LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): Result<ActorModel> {
        return if (isSuccessful) {
            Result.success(user1_test)
        } else {
            Result.failure(RuntimeException("Boom..."))
        }
    }
}