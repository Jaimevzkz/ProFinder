package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import javax.inject.Inject


interface TurnJobIntoRequestUseCase {
    suspend operator fun invoke(request: JobModel)

}

class TurnJobIntoRequestUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUserUseCase: GetUserUseCase
) : TurnJobIntoRequestUseCase {
    override suspend operator fun invoke(request: JobModel) {
        val user = getUserUseCase()
        repository.turnRequestIntoJob(
            ownerNickname = user.nickname,
            uid = user.uid,
            request = request
        )
    }
}