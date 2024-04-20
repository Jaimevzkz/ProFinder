package com.vzkz.profinder.domain.model

import com.google.android.gms.maps.model.LatLng

data class LocationModel(
    val uid: String,
    val nickname: String,
    val location: LatLng
)