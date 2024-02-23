package com.vzkz.profinder.fake.usecases

import com.vzkz.profinder.domain.usecases.auth.LogoutUseCase


class FakeLogoutUseCase: LogoutUseCase {
    override suspend operator fun invoke() {
        TODO()
    }
}