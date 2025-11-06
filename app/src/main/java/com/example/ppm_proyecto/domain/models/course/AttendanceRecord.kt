package com.example.ppm_proyecto.domain.models.course


/**
 * Clase que representa un registro de asistencia de un estudiante a una sesión de un curso.
 */
data class AttendanceRecord(
    val date: String,
    val status: AttendanceStatus
)

