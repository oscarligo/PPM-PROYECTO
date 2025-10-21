package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.model.AttendanceRecord
import com.example.ppm_proyecto.domain.model.AttendanceStatus
import com.example.ppm_proyecto.domain.model.Course
import com.example.ppm_proyecto.domain.model.CourseAttendance

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

// Cursos de ejemplo y asistencia por curso
val sampleCourseA = Course(id = "C-A", name = "Matemáticas")
val sampleCourseB = Course(id = "C-B", name = "Historia")

fun sampleCourseAttendance(): List<CourseAttendance> = listOf(
    CourseAttendance(
        course = sampleCourseA,
        records = listOf(
            AttendanceRecord("2024-07-01", AttendanceStatus.Presente),
            AttendanceRecord("2024-07-02", AttendanceStatus.Presente),
            AttendanceRecord("2024-07-03", AttendanceStatus.Ausente),
            AttendanceRecord("2024-07-04", AttendanceStatus.Tarde)
        )
    ),
    CourseAttendance(
        course = sampleCourseB,
        records = listOf(
            AttendanceRecord("2024-07-01", AttendanceStatus.Presente),
            AttendanceRecord("2024-07-02", AttendanceStatus.Ausente),
            AttendanceRecord("2024-07-03", AttendanceStatus.Ausente),
            AttendanceRecord("2024-07-04", AttendanceStatus.Presente)
        )
    )
)
