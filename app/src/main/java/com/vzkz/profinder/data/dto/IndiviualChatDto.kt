package com.vzkz.profinder.data.dto

data class IndiviualChatDto(
    val message: String,
    val timestamp: Long,
    val read: Int,
    val senderUid: String
)