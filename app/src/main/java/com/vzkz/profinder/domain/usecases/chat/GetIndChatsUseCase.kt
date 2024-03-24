package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetIndChatsUseCase {
    suspend operator fun invoke(otherUid: String): Flow<List<ChatMsgModel>>
}


class  GetIndChatsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetIndChatsUseCase {
    override suspend operator fun invoke(otherUid: String): Flow<List<ChatMsgModel>> {
        return repository.getIndividualChat(getUidDataStoreUseCase(), otherUid)
    }
}