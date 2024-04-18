package com.vzkz.profinder.domain.usecases.auth

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject
import com.vzkz.profinder.domain.error.Result


interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<ActorModel, FirebaseError>
}
class LoginUseCaseImpl @Inject constructor(private val repository: Repository): LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): Result<ActorModel, FirebaseError> {
        return repository.login(email, password)
    }
}