package com.vzkz.profinder.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ChatMsgModel(
    val msgId: String,
    val msg: String,
    val timestamp: Long,
    val isMine: Boolean
)

enum class ReadStatus(val icon: ImageVector, val tint: Color = Color.Gray) {
    Unread(icon = Icons.Filled.DoneAll),
    Read(icon = Icons.Filled.DoneAll, tint = Color.Blue)
}
