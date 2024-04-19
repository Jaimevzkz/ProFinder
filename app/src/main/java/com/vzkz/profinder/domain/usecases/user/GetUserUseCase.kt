package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton.Companion.getUserInstance
import com.vzkz.profinder.domain.model.ActorModel
import javax.inject.Inject

interface GetUserUseCase {
    suspend operator fun invoke(): Result<ActorModel, FirebaseError.Firestore>}


class GetUserUseCaseImpl @Inject constructor(
    repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetUserUseCase {
    private val instance = getUserInstance(repository)
    override suspend operator fun invoke(): Result<ActorModel, FirebaseError.Firestore> {
        return if (!instance.cachedUser()) {
            instance.getData(getUidDataStoreUseCase())
        } else instance.getData()
    }
}