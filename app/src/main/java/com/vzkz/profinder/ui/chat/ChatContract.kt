package com.vzkz.profinder.ui.chat

import com.vzkz.profinder.core.boilerplate.IndividualChatntent
import com.vzkz.profinder.core.boilerplate.State
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.UiError


data class ChatState(
    val loading: Boolean,
    val error: UiError,
    val chatList: List<ChatListItemModel>,
    val uid: String
) : State {
    companion object {
        val initial: ChatState = ChatState(
            loading = true,
            error = UiError(false, null),
            chatList = emptyList(),
            uid = ""
        )
    }
}

sealed class ChatIntent: IndividualChatntent {
    data object Loading: ChatIntent()
    data class Error(val errorMsg: String): ChatIntent()
    data object CloseError: ChatIntent()

    data class UpdateList(val newChatList: List<ChatListItemModel>): ChatIntent()
    data class SetUid(val uid: String): ChatIntent()
}