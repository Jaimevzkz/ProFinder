package com.vzkz.profinder.domain.usecases.requests

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface DeleteRequestUseCase {
    suspend operator fun invoke(sid: String)
    suspend fun deleteWithRid(rid: String, otherUid: String)}


class DeleteRequestUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val getRequestsUseCase: GetRequestsUseCase
) : DeleteRequestUseCase {
    override suspend operator fun invoke(sid: String) {
        val requestList = getRequestsUseCase().first()
        for (request in requestList) {
            if (request.serviceId == sid){

                repository.deleteRequest(uid = getUidDataStoreUseCase(), otherUid = request.otherUid, rid = request.rid)
            }
        }
    }

    override suspend fun deleteWithRid(rid: String, otherUid: String){
        repository.deleteRequest(uid = getUidDataStoreUseCase(), otherUid = otherUid, rid = rid)
    }
}