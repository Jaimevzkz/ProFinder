package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.singletons.ServiceListSingleton
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton
import javax.inject.Inject

interface ChangeStateUseCase {
    suspend operator fun invoke(uid: String, state: ProfState)
}

class ChangeStateUseCaseImpl @Inject constructor(private val repository: Repository) :
    ChangeStateUseCase {
    private val instance = UserDataSingleton.getUserInstance(repository)
    override suspend operator fun invoke(uid: String, state: ProfState) {
        repository.changeProfState(uid, state)
        instance.flushCache()
    }
}