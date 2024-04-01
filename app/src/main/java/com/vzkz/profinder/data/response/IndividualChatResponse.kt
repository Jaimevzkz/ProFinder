package com.vzkz.profinder.data.response

import com.vzkz.profinder.core.Constants.NULL_REALTIME_USERDATA
import com.vzkz.profinder.domain.model.ChatMsgModel

class IndividualChatResponse(
    val message: String? = null,
    val timestamp: Long? = null,
    val senderUid: String? = null
) {
    fun toDomain(msgId: String, ownerId: String): ChatMsgModel =
        ChatMsgModel(
            msgId = msgId,
            msg = message ?: throw Exception(NULL_REALTIME_USERDATA),
            timestamp = timestamp ?: throw Exception(NULL_REALTIME_USERDATA),
            isMine = senderUid == ownerId
        )
}