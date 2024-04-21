package com.vzkz.profinder.domain.model

data class JobModel(
    val id: String,
    val otherNickname: String,
    val otherUid: String,
    val serviceId: String,
    val serviceName: String,
    val price: Double,
    val isRatingPending: Boolean = false
)