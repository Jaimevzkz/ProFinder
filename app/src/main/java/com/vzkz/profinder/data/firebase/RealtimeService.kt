package com.vzkz.profinder.data.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import com.vzkz.profinder.core.Constants.CHATS
import com.vzkz.profinder.core.Constants.LAST_MSG
import com.vzkz.profinder.core.Constants.LAST_MSG_UID
import com.vzkz.profinder.core.Constants.PARTCIPANTS
import com.vzkz.profinder.core.Constants.RECENT_CHATS
import com.vzkz.profinder.core.Constants.TIMESTAMP
import com.vzkz.profinder.core.Constants.UNREAD_MSG_NUMBER
import com.vzkz.profinder.data.dto.IndiviualChatDto
import com.vzkz.profinder.data.dto.ParticipantDataDto
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
            val ref = realtimeDB.child(RECENT_CHATS).child(chatId)
            ref.setValue(chatDto)
        }
    }

    fun getRecentChats(ownerUid: String): Flow<List<ChatListItemModel>> {
        return realtimeDB.child(RECENT_CHATS)
            .orderByChild(TIMESTAMP).snapshots.map { dataSnapshot ->
                dataSnapshot.children.mapNotNull {
                    val chat = it.getValue(RecentFinalChatResponse::class.java)
                    if (chat?.participants?.keys?.contains(ownerUid) == true)
                        chat.toDomain(ownerUid)
                    else
                        null
                }

            }

    }

    fun updateRecentChats(
        chatId: String?,
        message: String,
        timestamp: Long,
        senderUid: String,
        participants: Map<String, ParticipantDataDto>
    ) {
        val finalChatId: String = chatId ?: realtimeDB.child(RECENT_CHATS).push().key.toString()

        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(PARTCIPANTS).setValue(participants)
        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(LAST_MSG).setValue(message)
        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(TIMESTAMP).setValue(timestamp)
        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(LAST_MSG_UID).get()
            .addOnSuccessListener { dataSnapshot ->
                dataSnapshot.getValue(String::class.java)?.let { lastMsgUid ->
                    if (lastMsgUid != senderUid) {
                        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(UNREAD_MSG_NUMBER)
                            .setValue(1)
                        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(LAST_MSG_UID)
                            .setValue(senderUid)
                    } else {
                        realtimeDB.child(RECENT_CHATS).child(finalChatId).child(UNREAD_MSG_NUMBER)
                            .get().addOnSuccessListener { dataSnapshot2 ->
                                dataSnapshot2.getValue(Int::class.java)?.let { unreadMsgNum ->
                                    realtimeDB.child(RECENT_CHATS).child(finalChatId)
                                        .child(UNREAD_MSG_NUMBER).setValue(unreadMsgNum + 1)
                                }
                            }
                    }
                }
            }


    }

    fun openRecentChat(chatId: String) {
        realtimeDB.child(RECENT_CHATS).child(chatId).child(UNREAD_MSG_NUMBER).setValue(0)
    }

    //Individual chat
    fun getChats(combinedUid: String, ownerUid: String): Flow<List<ChatMsgModel>> {
        return realtimeDB.child(CHATS).child(combinedUid)
            .orderByChild(TIMESTAMP).snapshots.map { dataSnapshot ->
                dataSnapshot.children.mapNotNull {
                    it.getValue(IndividualChatResponse::class.java)
                        ?.toDomain(msgId = it.key!!, ownerId = ownerUid)
                }
            }
    }

    fun addNewMessage(combinedUid: String, indiviualChatDto: IndiviualChatDto) {
        val ref = realtimeDB.child(CHATS).child(combinedUid).push()
        ref.setValue(indiviualChatDto)
    }
}