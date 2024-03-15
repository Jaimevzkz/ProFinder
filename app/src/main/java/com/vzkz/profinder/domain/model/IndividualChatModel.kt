package com.vzkz.profinder.domain.model

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class IndividualChatModel(
    val profilePhoto: Uri?,
    val nickname: String,
    val msgList: List<ChatMsgModel>
)

data class ChatMsgModel(
    val chatId: String,
    val msg: String,
    val timestamp: Long,
    val read: ReadStatus,
    val isMine: Boolean
)

enum class ReadStatus(val icon: ImageVector, val tint: Color = Color.Gray) {
    Sent(icon = Icons.Filled.Done),
    Unread(icon = Icons.Filled.DoneAll),
    Read(icon = Icons.Filled.DoneAll, tint = Color.Blue)
}