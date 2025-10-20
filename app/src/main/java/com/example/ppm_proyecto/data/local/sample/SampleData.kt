package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.model.AttendanceRecord
import com.example.ppm_proyecto.domain.model.AttendanceStatus

data class SampleStudent(
    val name: String,
    val profileImageUrl: String // En un futuro, podrías usar una URL real
)

// --- Datos de ejemplo para la UI ---

val sampleStudent = SampleStudent(
    name = "USUARIO",
    profileImageUrl = ""
)

val sampleAttendance = listOf(
    AttendanceRecord("24 de Julio, 2024", AttendanceStatus.Presente),
    AttendanceRecord("22 de Julio, 2024", AttendanceStatus.Presente),
    AttendanceRecord("20 de Julio, 2024", AttendanceStatus.Ausente),
    AttendanceRecord("17 de Julio, 2024", AttendanceStatus.Tarde),
    AttendanceRecord("15 de Julio, 2024", AttendanceStatus.Presente),
    AttendanceRecord("13 de Julio, 2024", AttendanceStatus.Presente),
)
