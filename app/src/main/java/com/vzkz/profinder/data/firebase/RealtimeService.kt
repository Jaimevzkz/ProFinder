package com.vzkz.profinder.data.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.snapshots
import com.vzkz.profinder.core.Constants.RECENT_CHATS
import com.vzkz.profinder.data.dto.RecentChatDto
import com.vzkz.profinder.data.response.RecentFinalChatResponse
import com.vzkz.profinder.domain.model.ChatListItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealtimeService @Inject constructor(private val realtimeDB: DatabaseReference) {
    fun addRecentChat(ownerUid: String, otherUid: String, chatDto: RecentChatDto) {

        val ref = realtimeDB.child(RECENT_CHATS).child(ownerUid).child(otherUid)
        ref.setValue(chatDto)
    }

    fun getRecentChats(ownerUid: String): Flow<List<ChatListItemModel>> { //returns all recent chats
       return realtimeDB.child(RECENT_CHATS).snapshots.map {dataSnapshot ->
           dataSnapshot.children.mapNotNull {
              val chat = it.getValue(RecentFinalChatResponse::class.java)
               if(chat?.participants?.keys?.contains(ownerUid) == true)
                   chat.toDomain(ownerUid)
               else
                   null
           }

       }

    }
}
