package com.vzkz.profinder.domain.model

import android.net.Uri

data class ChatListItemModel(
    val profilePhoto : Uri?,
    val nickname: String,
    val uid: String,
    val timestamp: Long,
    val lastMsg: String,
    val unreadMsgNumber: Int,
    val lastMsgUid: String
)
