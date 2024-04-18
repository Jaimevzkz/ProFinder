package com.vzkz.profinder.ui.chat

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.DateFormatter
import com.vzkz.profinder.core.UidCombiner
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.chat.GetRecentChatsUseCase
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import com.vzkz.profinder.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val getRecentChatsUseCase: GetRecentChatsUseCase,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val uidCombiner: UidCombiner
) : BaseViewModel<ChatState, ChatIntent>(ChatState.initial) {

    override fun reduce(state: ChatState, intent: ChatIntent): ChatState {
        return when (intent) {
            is ChatIntent.Loading -> state.copy(
                loading = true
            )


            is ChatIntent.Error -> state.copy(
                error = intent.error,
                loading = false
            )

            ChatIntent.CloseError -> state.copy(
                error = null,
                loading = false
            )

            is ChatIntent.UpdateList -> state.copy(
                chatList = intent.newChatList,
                loading = false
            )

            is ChatIntent.SetUid -> state.copy(uid = intent.uid)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI

    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            dispatch(ChatIntent.SetUid(getUidDataStoreUseCase()))
            when(val flow = getRecentChatsUseCase()){
                is Result.Success -> {
                    flow.data.collect { updatedList ->
                        dispatch(ChatIntent.UpdateList(updatedList.reversed()))
                    }
                }
                is Result.Error -> dispatch(ChatIntent.Error(flow.error.asUiText()))
            }
        }
    }

    fun onCloseDialog() = dispatch(ChatIntent.CloseError)

    fun getFormattedTime(timestamp: Long): String {
        return dateFormatter.formatToTime(timestamp)
    }

    fun combineUids(uid1: String, uid2: String) = uidCombiner.combineUids(uid1 = uid1, uid2 = uid2)
}