package com.example.ppm_proyecto.domain.models.course

import com.google.firebase.Timestamp

/**
 *  Estados posibles de asistencia a una sesión de curso.
 */
enum class AttendanceStatus(val displayName: String) {
    Absent("Ausente"),
    Late("Tarde"),
    Present("Presente");

}

/**
 * Clase que representa la asistencia de un estudiante a una sesión de un curso.
 */

data class SessionAttendance(
    val id : String = "",            // Identificador interno si se requiere
    val studentId: String = "",
    val courseId: String = "",       // Curso al que pertenece la sesión (faltaba para queries)
    val sessionId: String = "",      // Id de la sesión (documento de la sesión)
    val status: String = AttendanceStatus.Absent.name,
    val entryTime: Timestamp? = Timestamp.now(),
)
