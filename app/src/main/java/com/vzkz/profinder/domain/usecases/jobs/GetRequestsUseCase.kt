package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetRequestsUseCase {
    suspend operator fun invoke(isRequest: Boolean): Flow<List<JobModel>>}


class GetRequestsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetRequestsUseCase {
    override suspend operator fun invoke(isRequest: Boolean): Flow<List<JobModel>> {
        return repository.getJobsOrRequests(isRequest = isRequest, uid = getUidDataStoreUseCase())
    }
}