package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String): UserModel? {
        val result = repository.login(email, password)
        if (result != null && !result.second){ //Insert user to Room
            repository.insertUserToDB(result.first!!)
        }
        return result?.first
    }
}