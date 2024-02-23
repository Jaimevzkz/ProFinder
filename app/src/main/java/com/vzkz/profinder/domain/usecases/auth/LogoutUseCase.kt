package com.vzkz.profinder.domain.usecases.auth

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.usecases.FlushSingletonsUseCase
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val flushSingletonsUseCase: FlushSingletonsUseCase
) : LogoutUseCase {
    override suspend operator fun invoke() {
        flushSingletonsUseCase()
        return repository.logout()
    }
}