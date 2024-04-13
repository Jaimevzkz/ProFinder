package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.model.singletons.ProfileViewUserSingleton.Companion.getUserProfileToSeeInstance
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import javax.inject.Inject


interface RateJobUseCase {
    suspend operator fun invoke(job: JobModel, rating: Int)
}

class RateJobUseCaseUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : RateJobUseCase {

    private val profileToSeeInstance = getUserProfileToSeeInstance(repository)
    override suspend operator fun invoke(job: JobModel, rating: Int) {
        val uid = getUidDataStoreUseCase()
        repository.deleteJobOrRequest( //Delete the job
            isRequest = false,
            uid = uid,
            otherUid = job.otherUid,
            id = job.id
        )

        repository.updateRating(uid = job.otherUid, newRating = rating) //Update the rating

        profileToSeeInstance.flushCache() //flush the singleton so that the profile view is updated
    }
}