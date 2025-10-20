package com.example.ppm_proyecto.domain.model

// --- Modelo de datos para la lista (simulación) ---
enum class AttendanceStatus(val displayName: String) {
    Presente("Presente"),
    Ausente("Ausente"),
    Tarde("Llegada Tarde")
}

data class AttendanceRecord(
    val date: String,
    val status: AttendanceStatus
)
