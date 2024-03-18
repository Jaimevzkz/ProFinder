package com.vzkz.profinder.data.response

import android.net.Uri
import com.vzkz.profinder.domain.model.ChatListItemModel

data class RecentFinalChatResponse(
    val uid1: String? = null,
    val profilePhoto1 : Uri? = null,
    val nickname1: String? = null,
    val uid2: String? = null,
    val profilePhoto2 : Uri? = null,
    val nickname2: String? = null,
    val timestamp: Long? = null,
    val lastMsg: String? = null,
    val unreadMsgNumber: Int? = null,
    val lastMsgUid: String? = null,
){
    fun toDomain(ownerId: String) = ChatListItemModel(
        profilePhoto = if(uid1 == ownerId) profilePhoto2 else profilePhoto1,
        nickname = nickname ?: "Guess",
        timestamp = timestamp ?: 0,
        lastMsg = lastMsg ?: "No message",
        unreadMsgNumber = unreadMsgNumber ?: 0,
        lastMsgUid = lastMsgUid ?: "error"
    )
}