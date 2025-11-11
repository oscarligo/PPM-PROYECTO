package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import java.time.LocalDate

// Representa el estado actual de la pantalla CourseDetailsTeacherScreen
data class CourseDetailsTeacherState(
    val isLoading: Boolean = false,               // Indica si se está cargando información desde Firebase
    val errorMessage: String? = null,             // Guarda errores de red o de Firebase
    val courseId: String? = null,                 // Id del curso cargado (útil para refresh)
    val selectedDate: LocalDate = LocalDate.now(),// Fecha seleccionada para ver asistencia
    val courseName: String = "",                  // Nombre del curso mostrado
    val teacherName: String = "",                 // Nombre del maestro
    val students: List<StudentAttendance> = emptyList() // Lista de estudiantes con su estado de asistencia
)
