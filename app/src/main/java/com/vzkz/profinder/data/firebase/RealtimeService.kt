package com.vzkz.profinder.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.snapshots
import com.vzkz.profinder.core.Constants.RECENT_CHATS
import com.vzkz.profinder.core.Constants.RECENT_CHATS_BODY
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.response.RecentChatBodyResponse
import com.vzkz.profinder.data.response.RecentChatResponse
import com.vzkz.profinder.data.response.RecentFinalChatResponse
import com.vzkz.profinder.domain.model.ChatListItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealtimeService @Inject constructor(private val realtimeDB: DatabaseReference) {
    fun addRecentChat(ownerUid: String, otherUid: String, chatDto: RecentChatDto) {

        val ref = realtimeDB.child(RECENT_CHATS).child(ownerUid).child(otherUid)
        ref.setValue(chatDto)
    }

//    fun getRecentChats(ownerUid: String): Flow<List<ChatListItemModel>>{
//        val recentMsgList = mutableListOf<ChatListItemModel>()
//        val recentMessageFlow = MutableStateFlow<List<ChatListItemModel>>(recentMsgList)
//        realtimeDB.child(RECENT_CHATS).child(ownerUid).snapshots.map { dataSnapshot ->
//            dataSnapshot.children.mapNotNull{mapDataSnapshot ->
//                val recentChat = mapDataSnapshot.getValue(RecentChatResponse::class.java)
//                val chatId = recentChat?.chatId ?: throw Exception()
//                realtimeDB.child(RECENT_CHATS_BODY).child(chatId).snapshots.map {dataSnapshot2 ->
//                    val recentChatBody = dataSnapshot2.getValue(RecentChatBodyResponse::class.java)
////                    dataSnapshot2.children.mapNotNull{mapDataSnapshot2 ->
////                        val recentChatBody = mapDataSnapshot2.getValue(RecentChatBodyResponse::class.java)
////                    }
//                    val finalResponse = RecentFinalChatResponse(
//                        profilePhoto = recentChat.profilePhoto,
//                        nickname = recentChat.nickname,
//                        timestamp = recentChatBody?.timestamp,
//                        lastMsg = recentChatBody?.lastMsg,
//                        unreadMsgNumber = recentChatBody?.unreadMsgNumber,
//                        lastMsgUid = recentChatBody?.lastMsgUid
//                    )
//                    recentMsgList.add(finalResponse.toDomain())
//                }
//            }
//        }
//        return recentMessageFlow
//    }

    fun getRecentChats(ownerUid: String): Flow<List<ChatListItemModel>> {
        val recentMessageFlow = MutableStateFlow<List<ChatListItemModel>>(emptyList())

        realtimeDB.child(RECENT_CHATS).child(ownerUid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val recentMsgList = mutableListOf<ChatListItemModel>()
                for (mapDataSnapshot in dataSnapshot.children) {
                    val chatId = mapDataSnapshot.key
                    if (chatId != null) {
                        realtimeDB.child(RECENT_CHATS_BODY).child(chatId).get().addOnSuccessListener { dataSnapshot2 ->
                            val recentChatBody = dataSnapshot2.getValue(RecentChatBodyResponse::class.java)
                            val finalResponse = RecentFinalChatResponse(
                                profilePhoto = null, // As per the new structure, profilePhoto and nickname are not available
                                nickname = null, // As per the new structure, profilePhoto and nickname are not available
                                timestamp = recentChatBody?.timestamp,
                                lastMsg = recentChatBody?.lastMsg,
                                unreadMsgNumber = recentChatBody?.unreadMsgNumber,
                                lastMsgUid = recentChatBody?.lastMsgUid
                            )
                            recentMsgList.add(finalResponse.toDomain())
                            recentMessageFlow.value = recentMsgList
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        return recentMessageFlow
    }
}



//        return realtimeDB.child(RECENT_CHATS).child(ownerUid).orderByChild(TIMESTAMP).snapshots.map { dataSnapshot ->
//            dataSnapshot.children.mapNotNull {
//                val recentChat = it.getValue(RecentChatResponse::class.java).let {
//                    realtimeDB.child("recentChatBody").child(it.chatId!!).snapshots.map {
//                        it.children.mapNotNull {
//                            it.getValue(RecentChatBodyResponse::class.java).let {
//                                RecentFinalChatResponse(
//                                    profilePhoto = null,
//                                    nickname = null,
//                                    timestamp = null,
//                                    lastMsg = null,
//                                    unreadMsgNumber = null,
//                                    isLastMessageMine = null
//                                ).toDomain()
//                            }
//                        }
//                    }
//
//                }
//            }
//        }


//        getRecentChatsBody(ownerUid).collect{ recentChatResponseList ->
//            recentChatResponseList.map { recentChatResponse ->
//                val chatId = recentChatResponse.chatId ?: throw Exception()
//                realtimeDB.child("recentBodyChat").child(chatId).snapshots.map {dataSnapshot ->
//                    dataSnapshot.getValue(RecentChatBodyResponse::class.java).let {recentChatBodyResponse ->
//                        RecentFinalChatResponse(
//                            profilePhoto = recentChatResponse.profilePhoto,
//                            nickname = recentChatResponse.nickname,
//                            timestamp = recentChatBodyResponse?.timestamp,
//                            lastMsg = recentChatBodyResponse?.lastMsg,
//                            unreadMsgNumber = recentChatBodyResponse?.unreadMsgNumber,
//                            lastMsgUid = recentChatBodyResponse?.lastMsgUid
//                        ).toDomain()
//                    }
//                }
//            }
//
//        }
