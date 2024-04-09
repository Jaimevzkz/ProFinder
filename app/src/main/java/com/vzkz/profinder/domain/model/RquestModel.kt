package com.vzkz.profinder.domain.model

data class RequestModel(
    val rid: String,
    val otherNickname: String,
    val otherUid: String,
    val serviceId: String,
    val serviceName: String,
    val price: Double
)