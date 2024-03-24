package com.vzkz.profinder.data.response

import android.net.Uri
import com.vzkz.profinder.core.Constants.NULL_REALTIME_USERDATA
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
        return ChatListItemModel(
            chatId = chatId ?: throw Exception(NULL_REALTIME_USERDATA),
            uid = participant?.uid ?: throw Exception(NULL_REALTIME_USERDATA),
            profilePhoto = participant.let { if(it?.profilePhoto != null) Uri.parse(it.profilePhoto) else null },
            nickname = participant?.nickname ?: throw Exception(NULL_REALTIME_USERDATA),
            timestamp = timestamp ?: 0,
            lastMsg = lastMsg ?: throw Exception(NULL_REALTIME_USERDATA),
            unreadMsgNumber = unreadMsgNumber ?: throw Exception(NULL_REALTIME_USERDATA),
            lastMsgUid = lastMsgUid ?: throw Exception(NULL_REALTIME_USERDATA)
        )
    }
}

data class ParticipantDataResponse(
    var uid: String? = null,
    val profilePhoto: String? = null,
    val nickname: String? = null
)