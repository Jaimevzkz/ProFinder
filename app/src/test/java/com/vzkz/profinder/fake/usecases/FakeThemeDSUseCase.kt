package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.usecases.ThemeDSUseCase
import kotlinx.coroutines.flow.Flow

class FakeThemeDSUseCase : ThemeDSUseCase {
    override suspend fun switchTheme() {
        TODO()
    }

    override suspend operator fun invoke(): Flow<Boolean> {
        TODO()
    }
}