package com.vzkz.profinder.domain.usecases.user

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.singletons.ProfileViewUserSingleton.Companion.getUserProfileToSeeInstance
import javax.inject.Inject

interface UserProfileToSeeUseCase {
    suspend fun getUser(uid: String): Result<ActorModel, FirebaseError.Firestore>
    fun setUser(user: ActorModel)
}


class UserProfileToSeeUseCaseImpl @Inject constructor(
    repository: Repository
) : UserProfileToSeeUseCase {
    private val instance = getUserProfileToSeeInstance(repository)
    override suspend fun getUser(uid: String): Result<ActorModel, FirebaseError.Firestore> {
        return if (!instance.cachedUser(uid)) {
            instance.getData(uid)
        } else instance.getData()
    }

    override fun setUser(user: ActorModel) {
        instance.setUserData(user)
    }


}