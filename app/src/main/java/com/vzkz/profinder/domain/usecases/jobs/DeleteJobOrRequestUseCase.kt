package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface DeleteJobOrRequestUseCase {
    suspend operator fun invoke(isRequest: Boolean, sid: String)
    suspend fun deleteWithRid(isRequest: Boolean, id: String, otherUid: String)}


class DeleteJobOrRequestUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val getRequestsUseCase: GetRequestsUseCase
) : DeleteJobOrRequestUseCase {
    override suspend operator fun invoke(isRequest: Boolean, sid: String) {
        val requestList = getRequestsUseCase(isRequest = isRequest).first()
        for (request in requestList) {
            if (request.serviceId == sid) {

                repository.deleteJobOrRequest(
                    isRequest = isRequest,
                    uid = getUidDataStoreUseCase(),
                    otherUid = request.otherUid,
                    id = request.id
                )
            }
        }
    }

    override suspend fun deleteWithRid(isRequest: Boolean, id: String, otherUid: String) {
        repository.deleteJobOrRequest(
            isRequest = isRequest,
            uid = getUidDataStoreUseCase(),
            otherUid = otherUid,
            id = id
        )
    }
}