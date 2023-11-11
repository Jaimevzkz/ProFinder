package com.vzkz.profinder.domain.usecases

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.UserModel
import javax.inject.Inject

class LogoutUseCase@Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}