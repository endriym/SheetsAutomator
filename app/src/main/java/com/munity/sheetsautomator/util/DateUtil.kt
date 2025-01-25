package com.munity.sheetsautomator.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

object DateUtil {
    fun getCurrentDayMonth(): String {
        val date = Date.from(Instant.now())
        val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}