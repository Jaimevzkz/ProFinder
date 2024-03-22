package com.vzkz.profinder.data.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import com.vzkz.profinder.core.Constants.CHATS
import com.vzkz.profinder.core.Constants.RECENT_CHATS
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.response.IndividualChatResponse
import com.vzkz.profinder.data.response.RecentFinalChatResponse
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ChatMsgModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealtimeService @Inject constructor(private val realtimeDB: DatabaseReference) {
    //Recent chats
    fun addOrModifyRecentChat(chatId: String, chatDto: RecentChatDto) {
        if (chatId == "-1") {
            val ref = realtimeDB.child(RECENT_CHATS).push()
            ref.setValue(ref.key?.let { chatDto.copy(chatId = it) })
        } else {
            val ref = realtimeDB.child(RECENT_CHATS)
                .child(chatId) // PROBLEM: DISTINCTION BETWEEN ADDING AND UPDATING, WHAT ABOUT THE CHAT ID
            ref.setValue(chatDto)
        }
    }

    fun getRecentChats(ownerUid: String): Flow<List<ChatListItemModel>> {
        return realtimeDB.child(RECENT_CHATS).snapshots.map { dataSnapshot ->
            dataSnapshot.children.mapNotNull {
                val chat = it.getValue(RecentFinalChatResponse::class.java)
                if (chat?.participants?.keys?.contains(ownerUid) == true)
                    chat.toDomain(ownerUid)
                else
                    null
            }

        }

    }

    //Individual chat
    fun getChats(combinedUid: String, ownerid: String): Flow<List<ChatMsgModel>> {
        return realtimeDB.child(CHATS).child(combinedUid).snapshots.map { dataSnapshot ->
            dataSnapshot.children.mapNotNull {
                it.getValue(IndividualChatResponse::class.java)?.toDomain(msgId = it.key!!, ownerId = ownerid)
            }
        }
    }
}
