package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.usecases.LoginUseCase
import javax.inject.Inject


class FakeLoginUseCase @Inject constructor(private val repository: Repository): LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): ActorModel? {
        TODO("Not yet implemented")
    }
}