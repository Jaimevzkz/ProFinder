package com.vzkz.profinder.data.response

import com.vzkz.profinder.core.Constants.NULL_REALTIME_USERDATA
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.ReadStatus

class IndividualChatResponse(
    val message: String? = null,
    val timestamp: Long? = null,
    val read: Int? = null,
    val senderUid: String? = null
) {
    fun toDomain(msgId: String, ownerId: String): ChatMsgModel =
        ChatMsgModel(
            msgId = msgId,
            msg = message ?: throw Exception(NULL_REALTIME_USERDATA),
            timestamp = timestamp ?: throw Exception(NULL_REALTIME_USERDATA),
            read = when (read) {
                0 -> ReadStatus.Unread
                1 -> ReadStatus.Read
                else -> throw Exception(NULL_REALTIME_USERDATA)
            },
            isMine = senderUid == ownerId
        )
}