package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.singletons.UserDataSingleton.Companion.getUserInstance
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.singletons.UserListSingleton.Companion.getUserListInstance
import javax.inject.Inject

interface GetAllUsersUseCase {
    suspend operator fun invoke(): Result<List<ActorModel>, FirebaseError.Firestore>
}

class GetAllUsersUseCaseImpl @Inject constructor(
    repository: Repository,
) : GetAllUsersUseCase {
    private val instance = getUserListInstance(repository)
    override suspend operator fun invoke(): Result<List<ActorModel>, FirebaseError.Firestore> {
        return if (!instance.cachedServiceList()) {
            instance.getData()
        } else instance.getData()
    }
}