package com.vzkz.profinder.domain.usecases.chat

import android.net.Uri
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import javax.inject.Inject


interface AddNewMessageUseCase {

    suspend operator fun invoke(
        combinedUid: String,
        otherUid: String,
        otherNickname: String,
        otherProfilePicture: Uri?,
        msg: String
    )
}


class AddNewMessageUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val getUserUseCase: GetUserUseCase
) : AddNewMessageUseCase {
    override suspend operator fun invoke(
        combinedUid: String,
        otherUid: String,
        otherNickname: String,
        otherProfilePicture: Uri?,
        msg: String
    ) {
        val timestamp = System.currentTimeMillis()
        val actor = getUserUseCase()

        repository.addNewMessage(
            ownerUid = getUidDataStoreUseCase(),
            otherUid = otherUid,
            chatMsgModel = ChatMsgModel(
                msgId = "new",
                isMine = true,
                msg = msg,
                timestamp = timestamp,
            )
        )

        repository.updateRecentChat(
            combinedUid = combinedUid,
            message = msg,
            timestamp = timestamp,
            senderUid = actor.uid,
            participants = mapOf(
                actor.uid to ParticipantDataDto(
                    nickname = actor.nickname,
                    profilePhoto = actor.profilePhoto?.toString()
                ),
                otherUid to ParticipantDataDto(
                    nickname = otherNickname,
                    profilePhoto = otherProfilePicture?.toString()
                )
            )
        )
    }
}




















