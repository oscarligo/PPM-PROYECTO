package com.example.ppm_proyecto.domain.model


data class CourseSession(
    val id: String,
    val date: String,
    val topic: String,
    val attendanceRecords: List<User>
)