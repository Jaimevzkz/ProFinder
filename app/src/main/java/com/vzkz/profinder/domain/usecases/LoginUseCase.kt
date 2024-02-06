package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject


interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<ActorModel>
}
class LoginUseCaseImpl @Inject constructor(private val repository: Repository): LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): Result<ActorModel> {
        return repository.login(email, password)
    }
}