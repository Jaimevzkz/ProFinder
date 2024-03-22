package com.vzkz.profinder.data.dto

data class RecentChatDto(
    val participants: Map<String, ParticipantDataDto>,
    val timestamp: Long,
    val lastMsg: String?,
    val unreadMsgNumber: Int,
    val lastMsgUid: String,
    val chatId: String
)


data class ParticipantDataDto(
    val profilePhoto: String?,
    val nickname: String
)