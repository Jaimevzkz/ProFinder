package com.vzkz.profinder.domain.model

data class RequestModel(
    val rid: String,
    val clientNickname: String,
    val clientUid: String,
    val serviceId: String,
    val serviceName: String,
    val price: Double
)