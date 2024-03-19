package com.vzkz.profinder.data.response

import android.net.Uri
import com.vzkz.profinder.domain.model.ChatListItemModel

data class RecentFinalChatResponse(
    val participants: Map<String?, ParticipantData?>? = null,
    val timestamp: Long? = null,
    val lastMsg: String? = null,
    val unreadMsgNumber: Int? = null,
    val lastMsgUid: String? = null,
) {
    fun toDomain(ownerId: String): ChatListItemModel {
        var participant:ParticipantData? = null
        participants?.forEach {
            if(it.key != ownerId){
               participant = it.value
            }
        }
        return ChatListItemModel(
            profilePhoto = participant.let {  Uri.parse(it?.profilePhoto)},
            nickname = participant?.nickname ?: "Guess",
            timestamp = timestamp ?: 0,
            lastMsg = lastMsg ?: "No message",
            unreadMsgNumber = unreadMsgNumber ?: 0,
            lastMsgUid = lastMsgUid ?: "error"
        )
    }
}
data class ParticipantData(
    val profilePhoto: String? = null,
    val nickname: String? = null
)