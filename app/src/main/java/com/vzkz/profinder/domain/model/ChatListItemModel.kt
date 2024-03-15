package com.vzkz.profinder.domain.model

import android.net.Uri
import java.sql.Timestamp

data class ChatListItemModel(
    val nickname: String,
    val profilePhoto : Uri?,
    val timestamp: Long,
    val lastMsg: String,
    val isLastMessageMine: Boolean,
    val unreadMsgNumber: Int
)