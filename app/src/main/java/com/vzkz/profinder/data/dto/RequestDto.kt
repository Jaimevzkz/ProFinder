package com.vzkz.profinder.data.dto

import com.vzkz.profinder.core.Constants.CLIENT_ID
import com.vzkz.profinder.core.Constants.CLIENT_NICKNAME
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.SERVICE_ID
import com.vzkz.profinder.core.Constants.SERVICE_NAME

data class RequestDto(
    val clientNickname: String,
    val clientId: String,
    val serviceName: String,
    val serviceId: String,
    val price: Double
){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            CLIENT_NICKNAME to clientNickname,
            CLIENT_ID to clientId,
            SERVICE_NAME to serviceName,
            SERVICE_ID to serviceId,
            PRICE to price
        )
    }
}
