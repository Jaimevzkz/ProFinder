package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetRecentChatsUseCase {
    suspend operator fun invoke(): Flow<List<ChatListItemModel>>
}


class GetRecentChatsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
): GetRecentChatsUseCase {
    override suspend operator fun invoke(): Flow<List<ChatListItemModel>> {
        return repository.getRecentChats(getUidDataStoreUseCase())
    }
}