package com.example.ppm_proyecto.presentation.ui.coursedetails.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.SessionAttendance

/**
 * Estado de la pantalla de detalles del curso para estudiantes
 */
data class CourseDetailsStudentState(
    val isLoading: Boolean = true,
    val course: Course? = null,
    val attendanceRecords: List<SessionAttendance> = emptyList(),
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val lateCount: Int = 0,
    val attendancePercent: Int = 0,
    val error: String? = null,
    val userProfileUrl: String = "",
    val userName: String = ""
)