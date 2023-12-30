package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

interface SignUpUseCase {
    suspend operator fun invoke(email: String, password: String, nickname: String): UserModel?
}

class SignUpUseCaseImpl @Inject constructor(private val repository: Repository): SignUpUseCase {
    override suspend operator fun invoke(email: String, password: String, nickname: String): UserModel? {
        return repository.signUp(email, password, nickname)
    }
}