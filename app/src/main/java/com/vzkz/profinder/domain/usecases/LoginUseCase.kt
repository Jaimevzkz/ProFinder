package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject


interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): UserModel?
}
class LoginUseCaseImpl @Inject constructor(private val repository: Repository): LoginUseCase {
    override suspend operator fun invoke(email: String, password: String): UserModel? {
        return repository.login(email, password)
    }
}