package com.example.ppm_proyecto.presentation.ui.home.student

import com.example.ppm_proyecto.domain.model.Course
import com.example.ppm_proyecto.domain.model.Notification
import com.example.ppm_proyecto.domain.model.User


data class StudentHomeState(


    // Interfaz de Usuario
    val user: User? = null, // Usuario con sesión iniciada
    val userName: String = "", // Nombre del usuario
    val courses: List<Course> = emptyList(), // Cursos del estudiante
    val notifications: List<Notification> = emptyList(), // Notificaciones del estudiante
    val isLoading: Boolean = false, // Indicador de carga
    val error: String = "", // Mensaje de error si ocurre alguno
    val selectedNotification: String? = null, // ID de la notificación seleccionada
    val isDrawerOpen: Boolean = false, // Estado del drawer lateral de lo ajustes.

    // Estadísticas de asistencia
    val
    presentCount: Int = 0,
    val absentCount: Int = 0,
    val lateCount: Int = 0,

    // Gráficos de las estadísticas
    val attendancePercent: Int = 0, // Porcentaje de asistencia
    val barItems: List<Pair<String, Float>> = emptyList(), // Datos para el gráfico de barras

)