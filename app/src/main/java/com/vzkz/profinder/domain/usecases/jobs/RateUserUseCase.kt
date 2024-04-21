package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.singletons.ProfileViewUserSingleton.Companion.getUserProfileToSeeInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject


interface RateUserUseCase {
    suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore>
}

class RateUserUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : RateUserUseCase {

    private val profileToSeeInstance = getUserProfileToSeeInstance(repository)
    override suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore> {

        when(val update = repository.setRatingPending(uid = job.otherUid, jid = job.id)){
            is Result.Success -> {/*do nothing*/}
            is Result.Error -> return Result.Error(update.error)
        }

        when(val delete = repository.deleteIndividualJob(uid = getUidDataStoreUseCase(), jid = job.id)){
            is Result.Success -> {/*do nothing*/}
            is Result.Error -> return Result.Error(delete.error)
        }

        when (val update = repository.updateRating(
            uid = job.otherUid,
            newRating = rating
        )) {//Update the rating
            is Result.Success -> {/*do nothing*/}
            is Result.Error -> return Result.Error(update.error)
        }

        profileToSeeInstance.flushCache() //flush the singleton so that the profile view is updated (used for rating update)

        return Result.Success(Unit)
    }
}