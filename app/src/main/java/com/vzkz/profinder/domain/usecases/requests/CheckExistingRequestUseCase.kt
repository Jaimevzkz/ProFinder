package com.vzkz.profinder.domain.usecases.requests

import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface CheckExistingRequestUseCase {
    suspend operator fun invoke(sid: String): Boolean
}


class  CheckExistingRequestUseCaseImpl @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase
): CheckExistingRequestUseCase {
    override suspend operator fun invoke(sid: String): Boolean {
        val requestList = getRequestsUseCase().first()
        for (request in requestList){
            if(request.serviceId == sid)
                return true
        }
        return false
    }
}