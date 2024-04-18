package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.singletons.ProfileViewUserSingleton.Companion.getUserProfileToSeeInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject


interface RateJobUseCase {
    suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore>
}

class RateJobUseCaseUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : RateJobUseCase {

    private val profileToSeeInstance = getUserProfileToSeeInstance(repository)
    override suspend operator fun invoke(
        job: JobModel,
        rating: Int
    ): Result<Unit, FirebaseError.Firestore> {
        val uid = getUidDataStoreUseCase()
        when (val deletion = repository.deleteJobOrRequest( //Delete the job
            isRequest = false,
            uid = uid,
            otherUid = job.otherUid,
            id = job.id
        )) {
            is Result.Error -> return Result.Error(deletion.error)
            is Result.Success -> {/*do nothing*/
            }
        }


        when (val update = repository.updateRating(
            uid = job.otherUid,
            newRating = rating
        )) {//Update the rating
            is Result.Error -> return Result.Error(update.error)
            is Result.Success -> {/*do nothing*/}
        }

        profileToSeeInstance.flushCache() //flush the singleton so that the profile view is updated

        return Result.Success(Unit)
    }
}