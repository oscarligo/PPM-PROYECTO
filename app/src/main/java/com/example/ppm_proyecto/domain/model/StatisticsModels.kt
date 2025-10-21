package com.example.ppm_proyecto.domain.model

// Asistencia por sesión
data class AttendanceCounts(
    val present: Int = 0,
    val absent: Int = 0,
    val late: Int = 0
) {
    val total: Int get() = present + absent + late
}

// Asistencia por curso
data class CourseAttendance(
    val course: Course,
    val records: List<AttendanceRecord>
)



