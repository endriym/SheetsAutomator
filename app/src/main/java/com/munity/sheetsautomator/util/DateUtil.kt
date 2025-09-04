package com.munity.sheetsautomator.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

    fun getIso8601FromNowIn(seconds: Int): String =
        Instant.now().plusSeconds(seconds.toLong()).toString()

    fun iso8601ToInstant(iso8601Date: String): Instant {
        val formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault())
        return Instant.from(formatter.parse(iso8601Date))
    }

    fun isIso8601Expired(iso8601Date: String): Boolean =
        // Parse the ISO 8601 string to an Instant
        Instant.now() > iso8601ToInstant(iso8601Date)
}
