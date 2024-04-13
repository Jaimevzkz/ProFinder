package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetUnreadMessageAndOwnerUseCase {
    operator fun invoke(ownerUid: String, combinedUid: String): Flow<Pair<Boolean, Int>>
}


class GetUnreadMessageAndOwnerUseCaseImpl @Inject constructor(
    private val repository: Repository,
) : GetUnreadMessageAndOwnerUseCase {
    override operator fun invoke(
        ownerUid: String,
        combinedUid: String
    ): Flow<Pair<Boolean, Int>> = repository.getUnreadMsgAndOwner(ownerUid = ownerUid, combinedUid = combinedUid)
}