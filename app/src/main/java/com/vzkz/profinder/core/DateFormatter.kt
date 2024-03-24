package com.vzkz.profinder.core

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatter @Inject constructor(){
    fun formatToTime(timestamp: Long): String {
        val dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp), ZoneId.of("Europe/Madrid"))
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return dateTime.format(formatter)
    }

    fun getDate(tiemstamp: Long): String{
        val dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(tiemstamp), ZoneId.of("Europe/Madrid"))
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return dateTime.format(formatter)
    }
}