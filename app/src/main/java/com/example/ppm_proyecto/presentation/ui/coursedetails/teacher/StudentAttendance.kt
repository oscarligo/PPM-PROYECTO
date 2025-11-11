package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus

// Modelo pequeño compartido entre ViewModel y UI
data class StudentAttendance(
    val name: String,
    val status: AttendanceStatus
)
