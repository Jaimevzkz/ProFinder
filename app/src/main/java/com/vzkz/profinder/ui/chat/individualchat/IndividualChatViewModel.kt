package com.vzkz.profinder.ui.chat.individualchat

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.DateFormatter
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import com.vzkz.profinder.domain.usecases.chat.AddNewMessageUseCase
import com.vzkz.profinder.domain.usecases.chat.GetIndChatsUseCase
import com.vzkz.profinder.domain.usecases.chat.GetUnreadMessageAndOwnerUseCase
import com.vzkz.profinder.domain.usecases.chat.OpenRecentChatsUseCase
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IndividualChatViewModel @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val getIndChatsUseCase: GetIndChatsUseCase,
    private val addNewMessageUseCase: AddNewMessageUseCase,
    private val getUidDataStoreUseCase: GetUidDataStoreUseCase,
    private val openRecentChatsUseCase: OpenRecentChatsUseCase,
    private val getUnreadMessageAndOwnerUseCase: GetUnreadMessageAndOwnerUseCase
) :
    BaseViewModel<IndividualChatState, IndividualChatIntent>(IndividualChatState.initial) {

    override fun reduce(
        state: IndividualChatState,
        intent: IndividualChatIntent
    ): IndividualChatState {
        return when (intent) {
            is IndividualChatIntent.Loading -> state.copy(loading = true)

            is IndividualChatIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            IndividualChatIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )

            is IndividualChatIntent.UpdateList -> state.copy(
                loading = false,
                chatList = intent.updatedList
            )

            is IndividualChatIntent.UpdateUnreadMsgs -> state.copy(unreadMsgNumber = intent.unreadMsgs)
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit(otherUid: String, combinedUid: String) {
        getUnreadMessages(combinedUid = combinedUid)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                getIndChatsUseCase(otherUid).collect { chatList ->
                    dispatch(IndividualChatIntent.UpdateList(chatList))
                }
            }
        } catch (e: Exception) {
            dispatch(IndividualChatIntent.Error(e.message.orEmpty()))
        }
    }

    fun markAsRead(combinedUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            openRecentChatsUseCase(combinedUid)
        }
    }

    private fun getUnreadMessages(combinedUid: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                getUnreadMessageAndOwnerUseCase(
                    ownerUid = getUidDataStoreUseCase(),
                    combinedUid = combinedUid
                ).collect { unreadMsgs ->
                    if (unreadMsgs.first)
                        dispatch(IndividualChatIntent.UpdateUnreadMsgs(unreadMsgs.second))
                    else
                        dispatch(IndividualChatIntent.UpdateUnreadMsgs(0))
                }
            }
        } catch (e: Exception) {
            dispatch(IndividualChatIntent.Error(e.message.orEmpty()))
        }
    }

    fun getFormattedTime(timestamp: Long): String {
        return dateFormatter.formatToTime(timestamp)
    }

    fun getDate(timestamp: Long): String {
        return dateFormatter.getDate(timestamp)
    }

    fun onCloseDialog() = dispatch(IndividualChatIntent.CloseError)

    fun sendMessage(
        combinedUid: String,
        otherUid: String,
        otherNickname: String,
        otherProfilePicture: Uri?,
        msg: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            addNewMessageUseCase(
                msg = msg,
                combinedUid = combinedUid,
                otherUid = otherUid,
                otherNickname = otherNickname,
                otherProfilePicture = otherProfilePicture
            )
        }
    }
}