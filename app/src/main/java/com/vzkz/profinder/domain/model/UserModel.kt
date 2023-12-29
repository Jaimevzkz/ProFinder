package com.vzkz.profinder.domain.model

import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.UID

data class UserModel(val nickname: String, val uid: String){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            UID to uid,
            NICKNAME to nickname,
        )
    }
}