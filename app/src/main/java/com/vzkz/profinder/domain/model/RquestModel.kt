package com.vzkz.profinder.domain.model

data class RequestModel(
    val rid: String = "123",
    val clientNickname: String = "123",
    val clientUid: String = "123",
    val serviceId: String = "123",
    val serviceName: String = "123",
    val price: Double = 12.0
)