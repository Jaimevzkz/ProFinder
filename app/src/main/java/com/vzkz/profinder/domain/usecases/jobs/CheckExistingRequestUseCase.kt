package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface CheckExistingRequestUseCase {
    suspend operator fun invoke(sid: String): ServiceState
}

class CheckExistingRequestUseCaseImpl @Inject constructor(
    private val getRequestsUseCase: GetRequestsUseCase
) : CheckExistingRequestUseCase {
    override suspend operator fun invoke(sid: String): ServiceState {
        val requestList = getRequestsUseCase(isRequest = true).first()
        val jobList = getRequestsUseCase(isRequest = false).first()

        if (requestList.any { it.serviceId == sid })
            return ServiceState.REQUESTED

        if (jobList.any { it.serviceId == sid })
            return ServiceState.JOB

        return ServiceState.FREE
    }
}
