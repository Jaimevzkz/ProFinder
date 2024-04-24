package com.vzkz.profinder.ui.chat.individualchat

import com.vzkz.profinder.core.boilerplate.Intent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.ui.UiText


data class IndividualChatState(
    val loading: Boolean,
    val error: UiText?,
    val chatList: List<ChatMsgModel>,
    val unreadMsgNumber: Int
) : State {
    companion object {
        val initial: IndividualChatState = IndividualChatState(
            loading = false,
            error = null,
            chatList = emptyList(),
            unreadMsgNumber = 0
        )
    }
}

sealed class IndividualChatIntent: Intent {
    data object Loading: IndividualChatIntent()
    data class Error(val error: UiText): IndividualChatIntent()
    data object CloseError: IndividualChatIntent()
    data class UpdateList(val updatedList: List<ChatMsgModel>): IndividualChatIntent()
    data class UpdateUnreadMsgs(val unreadMsgs: Int): IndividualChatIntent()
}