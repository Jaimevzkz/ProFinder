package com.vzkz.profinder.domain.model

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class LocationModel(
    val uid: String,
    val profilePhoto: Uri?,
    val location: LatLng
)