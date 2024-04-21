package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.singletons.ProfileViewUserSingleton.Companion.getUserProfileToSeeInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject


interface RateProfUseCase {
    suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore>
}

class RateProfUseCaseUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : RateProfUseCase {

    private val profileToSeeInstance = getUserProfileToSeeInstance(repository)
    override suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore> {

        when(val delete = repository.deleteIndividualJob(uid = getUidDataStoreUseCase(), jid = job.id)){
            is Result.Success -> {/*do nothing*/}
            is Result.Error -> return Result.Error(delete.error)
        }


        when (val update = repository.updateRating(
            uid = job.otherUid,
            newRating = rating
        )) {//Update the rating
            is Result.Error -> return Result.Error(update.error)
            is Result.Success -> {/*do nothing*/}
        }

        profileToSeeInstance.flushCache() //flush the singleton so that the profile view is updated (used for rating update)

        return Result.Success(Unit)
    }
}