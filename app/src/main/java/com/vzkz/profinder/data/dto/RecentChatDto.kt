package com.vzkz.profinder.data.dto

import android.net.Uri

data class RecentChatDto(
    val profilePhoto : Uri?,
    val nickname: String,
    val chatId: Long,
)


data class DummyRecentChatDto(
    val timestamp: Long,
    val lastMsg: String,
    val unreadMsgNumber: Int,
    val lastMsgUid: String
)