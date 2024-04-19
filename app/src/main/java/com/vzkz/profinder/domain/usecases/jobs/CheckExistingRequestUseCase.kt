package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.ui.services.components.userscreen.ServiceState
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface CheckExistingRequestUseCase {
    suspend operator fun invoke(sid: String): Result<ServiceState, FirebaseError.Firestore>}

class CheckExistingRequestUseCaseImpl @Inject constructor(
    private val getRequestsUseCase: GetJobOrRequestsUseCase
) : CheckExistingRequestUseCase {
    override suspend operator fun invoke(sid: String): Result<ServiceState, FirebaseError.Firestore> {
        val requestList = when(val request = getRequestsUseCase(isRequest = true)){
            is Result.Success -> request.data.first()
            is Result.Error -> return Result.Error(request.error)
        }

        val jobList = when(val job = getRequestsUseCase(isRequest = false)){
            is Result.Success -> job.data.first()
            is Result.Error -> return Result.Error(job.error)
        }


        if (requestList.any { it.serviceId == sid })
            return Result.Success(ServiceState.REQUESTED)

        if (jobList.any { it.serviceId == sid })
            return Result.Success(ServiceState.JOB)

        return Result.Success(ServiceState.FREE)
    }
}
