package com.vzkz.profinder.core

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatter @Inject constructor(){
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToTime(timestamp: Long): String {
        val dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp), java.time.ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("HH:mm a")
        return dateTime.format(formatter)
    }
}