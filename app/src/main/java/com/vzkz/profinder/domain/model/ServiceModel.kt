package com.vzkz.profinder.domain.model

import com.vzkz.profinder.core.Constants.CATEGORY
import com.vzkz.profinder.core.Constants.IS_ACTIVE
import com.vzkz.profinder.core.Constants.NAME
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.SERV_DESCRIPTION
import com.vzkz.profinder.core.Constants.UID

data class ServiceModel(
    val sid: String,
    val uid: String,
    val name: String,
    val isActive: Boolean,
    val category: Categories,
    val servDescription: String,
    val owner : ActorModel,
    val price: Double
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            UID to uid,
            NAME to name,
            IS_ACTIVE to isActive,
            CATEGORY to category,
            SERV_DESCRIPTION to servDescription,
            PRICE to price
        )
    }
}