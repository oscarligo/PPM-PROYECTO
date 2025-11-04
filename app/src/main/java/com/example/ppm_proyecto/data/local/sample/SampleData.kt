package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.models.course.AttendanceRecord
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.UserRole

// Datos de ejemplo para la UI (usando modelos del dominio)

val sampleStudent = User(
    id = "stu-0",
    name = "USUARIO",
    email = "usuario@example.com",
    role = UserRole.Student,
    profileImageUrl = ""
)

val sampleAttendance = listOf(
    AttendanceRecord("2024-07-24", AttendanceStatus.Present),
    AttendanceRecord("2024-07-22", AttendanceStatus.Present),
    AttendanceRecord("2024-07-20", AttendanceStatus.Absent),
    AttendanceRecord("2024-07-17", AttendanceStatus.Late),
    AttendanceRecord("2024-07-15", AttendanceStatus.Present),
    AttendanceRecord("2024-07-13", AttendanceStatus.Present),
)
