package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GetRecentChatsUseCase {
    suspend operator fun invoke(): Result<Flow<List<ChatListItemModel>>, FirebaseError.Realtime>
}


class GetRecentChatsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase
) : GetRecentChatsUseCase {
    override suspend operator fun invoke(): Result<Flow<List<ChatListItemModel>>, FirebaseError.Realtime> =
        repository.getRecentChats(getUidDataStoreUseCase())

}