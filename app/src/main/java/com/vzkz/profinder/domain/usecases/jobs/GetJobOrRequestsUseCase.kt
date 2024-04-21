package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetJobOrRequestsUseCase {
    suspend operator fun invoke(isRequest: Boolean): Result<Flow<List<JobModel>>, FirebaseError.Firestore>
}


class GetJobOrRequestsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetJobOrRequestsUseCase {
    override suspend operator fun invoke(isRequest: Boolean): Result<Flow<List<JobModel>>, FirebaseError.Firestore> =
        repository.getJobsOrRequests(isRequest = isRequest, uid = getUidDataStoreUseCase())

}