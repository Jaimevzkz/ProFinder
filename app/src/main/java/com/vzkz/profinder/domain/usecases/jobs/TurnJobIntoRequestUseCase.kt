package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.JobModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import javax.inject.Inject


interface TurnJobIntoRequestUseCase {
    suspend operator fun invoke(request: JobModel): Result<Unit, FirebaseError.Firestore>
}

class TurnJobIntoRequestUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUserUseCase: GetUserUseCase
) : TurnJobIntoRequestUseCase {
    override suspend operator fun invoke(request: JobModel): Result<Unit, FirebaseError.Firestore> {
        return when(val user = getUserUseCase()){
            is Result.Success -> {
                repository.turnRequestIntoJob(
                    ownerNickname = user.data.nickname,
                    uid = user.data.uid,
                    request = request
                )
            }
            is Result.Error -> Result.Error(user.error)
        }
    }
}