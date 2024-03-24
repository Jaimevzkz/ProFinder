package com.vzkz.profinder.ui.chat.individualchat

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.UiError


data class IndividualChatState(
    val loading: Boolean,
    val error: UiError,
    val chatList: List<ChatMsgModel>
) : State {
    companion object {
        val initial: IndividualChatState = IndividualChatState(
            loading = false,
            error = UiError(false, null),
            chatList = emptyList(),
        )
    }
}

sealed class IndividualChatIntent: IndividualChatntent {
    data object Loading: IndividualChatIntent()
    data class Error(val errorMsg: String): IndividualChatIntent()
    data object CloseError: IndividualChatIntent()
    data class UpdateList(val updatedList: List<ChatMsgModel>): IndividualChatIntent()
}