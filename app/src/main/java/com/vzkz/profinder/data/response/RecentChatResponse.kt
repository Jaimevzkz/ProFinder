package com.vzkz.profinder.data.response

import android.net.Uri
import com.vzkz.profinder.domain.model.ChatListItemModel

data class RecentFinalChatResponse(
    val participants: Map<String?, ParticipantDataResponse?>? = null,
    val timestamp: Long? = null,
    val lastMsg: String? = null,
    val unreadMsgNumber: Int? = null,
    val lastMsgUid: String? = null,
    val chatId: String? = null
) {
    fun toDomain(ownerId: String): ChatListItemModel {
        var participant: ParticipantDataResponse? = null
        participants?.forEach {
            if (it.key != ownerId) {
                participant = it.value
                participant?.uid = it.key
            }
        }
        return ChatListItemModel( //todo handle errors
            chatId = chatId ?: "error",
            uid = participant?.uid ?: "error",
            profilePhoto = participant.let { Uri.parse(it?.profilePhoto) },
            nickname = participant?.nickname ?: "Guess",
            timestamp = timestamp ?: 0,
            lastMsg = lastMsg ?: "No message",
            unreadMsgNumber = unreadMsgNumber ?: 0,
            lastMsgUid = lastMsgUid ?: "error"
        )
    }
}

data class ParticipantDataResponse(
    var uid: String? = null,
    val profilePhoto: String? = null,
    val nickname: String? = null
)