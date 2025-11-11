package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.models.user.Notification
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


// Helper para crear un Firebase Timestamp a partir de una fecha en formato ISO (yyyy-MM-dd)
private fun ts(isoDate: String): Timestamp {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }
    return try {
        val date = sdf.parse(isoDate) ?: return Timestamp.now()
        Timestamp(date)
    } catch (_: ParseException) {
        Timestamp.now()
    } 
}

val sampleNotifications: List<Notification> = listOf(
    Notification(
        id = "n-001",
        title = "Recordatorio de clase",
        message = "Tienes limite de faltas!!!!!",
        date = ts("2024-10-04"),
        isRead = false
    ),
    Notification(
        id = "n-002",
        title = "Asistencia perfecta",
        message = "Muy bien! Tu asistencia es excelente.",
        date = ts("2024-10-05"),
        isRead = true
    )
)
