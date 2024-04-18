package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetUnreadMessageAndOwnerUseCase {
    operator fun invoke(
        ownerUid: String,
        combinedUid: String
    ): Result<Flow<Pair<Boolean, Int>>, FirebaseError.Realtime>
}


class GetUnreadMessageAndOwnerUseCaseImpl @Inject constructor(
    private val repository: Repository,
) : GetUnreadMessageAndOwnerUseCase {
    override operator fun invoke(
        ownerUid: String,
        combinedUid: String
    ): Result<Flow<Pair<Boolean, Int>>, FirebaseError.Realtime> =
        repository.getUnreadMsgAndOwner(ownerUid = ownerUid, combinedUid = combinedUid)
}