package com.vzkz.profinder.data.response

import com.vzkz.profinder.core.Constants
import com.vzkz.profinder.core.Constants.NULL_USERDATA
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.ReadStatus

class IndividualChatResponse(
    val message: String,
    val timestamp: Long,
    val read: Int,
    val senderUid: String
) {
    fun toDomain(msgId: String, ownerId: String): ChatMsgModel =
        ChatMsgModel(
            msgId = msgId,
            msg = message,
            timestamp = timestamp,
            read = when (read) {
                0 -> ReadStatus.Unread
                1 -> ReadStatus.Read
                else -> throw Exception(NULL_USERDATA)
            },
            isMine = senderUid == ownerId
        )
}