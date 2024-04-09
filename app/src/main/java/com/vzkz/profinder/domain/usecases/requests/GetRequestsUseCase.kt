package com.vzkz.profinder.domain.usecases.requests

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.RequestModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetRequestsUseCase {
    suspend operator fun invoke(): Flow<List<RequestModel>>
}


class GetRequestsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetRequestsUseCase {
    override suspend operator fun invoke(): Flow<List<RequestModel>> {
        return repository.getJobRequests(getUidDataStoreUseCase())
    }
}