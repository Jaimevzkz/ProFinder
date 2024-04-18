package com.vzkz.profinder.data.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.snapshots
import com.vzkz.profinder.core.Constants.CHATS
import com.vzkz.profinder.core.Constants.LAST_MSG
import com.vzkz.profinder.core.Constants.LAST_MSG_UID
import com.vzkz.profinder.core.Constants.PARTCIPANTS
import com.vzkz.profinder.core.Constants.REALTIME_ACCESS_INTERRUPTED
import com.vzkz.profinder.core.Constants.RECENT_CHATS
import com.vzkz.profinder.core.Constants.TIMESTAMP
import com.vzkz.profinder.core.Constants.UNREAD_MSG_NUMBER
import com.vzkz.profinder.data.dto.IndiviualChatDto
import com.vzkz.profinder.data.dto.ParticipantDataDto
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.response.IndividualChatResponse
import com.vzkz.profinder.data.response.RecentFinalChatResponse
import com.vzkz.profinder.domain.error.FirebaseError
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ChatMsgModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.vzkz.profinder.domain.error.Result

class RealtimeService @Inject constructor(private val realtimeDB: DatabaseReference) {
    //Recent chats
    fun addOrModifyRecentChat(combinedUid: String, chatDto: RecentChatDto) {
        val ref = realtimeDB.child(RECENT_CHATS).child(combinedUid)
        ref.setValue(chatDto)
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
        combinedUids: String,
        message: String,
        timestamp: Long,
        senderUid: String,
        participants: Map<String, ParticipantDataDto>
    ): Result<Unit, FirebaseError.Realtime> {
        try {
            realtimeDB.child(RECENT_CHATS).child(combinedUids).child(PARTCIPANTS).setValue(participants)
            realtimeDB.child(RECENT_CHATS).child(combinedUids).child(LAST_MSG).setValue(message)
            realtimeDB.child(RECENT_CHATS).child(combinedUids).child(TIMESTAMP).setValue(timestamp)

            realtimeDB.child(RECENT_CHATS).child(combinedUids).child(LAST_MSG_UID).get()
                .addOnSuccessListener { dataSnapshot ->
                    if(dataSnapshot.exists()){
                        dataSnapshot.getValue(String::class.java)?.let { lastMsgUid ->
                            if (lastMsgUid != senderUid) {
                                realtimeDB.child(RECENT_CHATS).child(combinedUids).child(UNREAD_MSG_NUMBER)
                                    .setValue(1)
                                realtimeDB.child(RECENT_CHATS).child(combinedUids).child(LAST_MSG_UID)
                                    .setValue(senderUid)
                            } else {
                                realtimeDB.child(RECENT_CHATS).child(combinedUids).child(UNREAD_MSG_NUMBER)
                                    .get().addOnSuccessListener { dataSnapshot2 ->
                                        dataSnapshot2.getValue(Int::class.java)?.let { unreadMsgNum ->
                                            realtimeDB.child(RECENT_CHATS).child(combinedUids)
                                                .child(UNREAD_MSG_NUMBER).setValue(unreadMsgNum + 1)
                                        }
                                    }
                            }
                        }
                    } else {
                        realtimeDB.child(RECENT_CHATS).child(combinedUids).child(UNREAD_MSG_NUMBER)
                            .setValue(1)
                        realtimeDB.child(RECENT_CHATS).child(combinedUids).child(LAST_MSG_UID)
                            .setValue(senderUid)
                    }

                }
                .addOnFailureListener{ //if it get here it means its a new chat
                    Log.e("Jaime", "Something went wrong while updating recent chats")
                    throw Exception()
                }
        } catch (e: Exception){
            return Result.Error(FirebaseError.Realtime.RECENT_CHAT_UPDATE_ERROR)
        }
        return Result.Success(Unit)
    }

    fun openRecentChat(combinedUid: String) {
        realtimeDB.child(RECENT_CHATS).child(combinedUid).child(UNREAD_MSG_NUMBER).setValue(0)
    }

    fun getUnreadMsgAndOwner(ownerUid: String, combinedUid: String): Flow<Pair<Boolean, Int>> =
        callbackFlow {
            val recentChatsRef = realtimeDB.child(RECENT_CHATS).child(combinedUid)
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lastMsgUid = snapshot.child(LAST_MSG_UID).getValue(String::class.java)
                    val unreadMsgNumber =
                        snapshot.child(UNREAD_MSG_NUMBER).getValue(Int::class.java)
                    val isMine = lastMsgUid == ownerUid
                    if (unreadMsgNumber != null)
                        trySend(Pair(isMine, unreadMsgNumber))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Jaime", error.message)
                    throw Exception(REALTIME_ACCESS_INTERRUPTED)
                }

            }

            recentChatsRef.addValueEventListener(valueEventListener)

            awaitClose {
                recentChatsRef.removeEventListener(valueEventListener)
            }
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
