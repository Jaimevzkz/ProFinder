package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject


interface DeleteJobOrRequestUseCase {
    suspend operator fun invoke(
        isRequest: Boolean,
        sid: String
    ): Result<Unit, FirebaseError.Firestore>

    suspend fun deleteWithRid(
        isRequest: Boolean,
        id: String,
        otherUid: String
    ): Result<Unit, FirebaseError.Firestore>
}

class DeleteJobOrRequestUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val getRequestsUseCase: GetJobOrRequestsUseCase
) : DeleteJobOrRequestUseCase {
    override suspend operator fun invoke(
        isRequest: Boolean,
        sid: String
    ): Result<Unit, FirebaseError.Firestore> {
        return when(val requestList = getRequestsUseCase(isRequest = isRequest)){
            is Result.Success -> {
                for (request in requestList.data.first()) {
                    if (request.serviceId == sid) {
                        when (val deletion = repository.deleteJobOrRequest(
                            isRequest = isRequest,
                            uid = getUidDataStoreUseCase(),
                            otherUid = request.otherUid,
                            id = request.id
                        )) {
                            is Result.Error -> return Result.Error(deletion.error)
                            is Result.Success -> {/*do nothing*/
                            }
                        }
                    }
                }
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(requestList.error)
        }
    }

    override suspend fun deleteWithRid(
        isRequest: Boolean,
        id: String,
        otherUid: String
    ): Result<Unit, FirebaseError.Firestore> = repository.deleteJobOrRequest(
        isRequest = isRequest,
        uid = getUidDataStoreUseCase(),
        otherUid = otherUid,
        id = id
    )

}