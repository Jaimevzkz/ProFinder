package com.vzkz.profinder.data.dto

import com.vzkz.profinder.core.Constants.OTHER_ID
import com.vzkz.profinder.core.Constants.OTHER_NICKNAME
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.SERVICE_ID
import com.vzkz.profinder.core.Constants.SERVICE_NAME

data class RequestDto(
    val profNickname: String,
    val otherNickname: String,
    val otherId: String,
    val serviceName: String,
    val serviceId: String,
    val price: Double
){
    fun toMapProf(): Map<String, Any?>{
        return mapOf(
            OTHER_NICKNAME to otherNickname,
            OTHER_ID to otherId,
            SERVICE_NAME to serviceName,
            SERVICE_ID to serviceId,
            PRICE to price
        )
    }
    fun toMapUser(profUid: String): Map<String, Any?>{
        return mapOf(
            OTHER_NICKNAME to profNickname,
            OTHER_ID to profUid,
            SERVICE_NAME to serviceName,
            SERVICE_ID to serviceId,
            PRICE to price
        )
    }
}
