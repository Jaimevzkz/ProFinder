package com.vzkz.profinder.domain.usecases.chat

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface OpenRecentChatsUseCase {
    suspend operator fun invoke(combinedUid: String)
}


class OpenRecentChatsUseCaseImpl @Inject constructor(
    private val repository: Repository,
): OpenRecentChatsUseCase {
    override suspend operator fun invoke(combinedUid: String){
        repository.openRecentChat(combinedUid)
    }
}