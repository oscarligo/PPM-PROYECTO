package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import java.time.DayOfWeek
import java.time.LocalDate
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.course.CourseSession

// Representa el estado actual de la pantalla CourseDetailsTeacherScreen
data class CourseDetailsTeacherState(
    val isLoading: Boolean = false,               // Indica si se está cargando información desde Firebase
    val errorMessage: String? = null,             // Guarda errores de red o de Firebase
    val courseId: String? = null,                 // Id del curso cargado (útil para refresh)
    val selectedDate: LocalDate = LocalDate.now(),// Fecha seleccionada para ver asistencia
    val courseName: String = "",                  // Nombre del curso mostrado
    val teacherName: String = "",                 // Nombre del maestro
    val students: List<SessionAttendance> = emptyList(), // Ahora usando modelo del dominio
    val sessions: List<CourseSession> = emptyList(),
    val selectedSessionId: String? = null,
    val studentNames: Map<String, String> = emptyMap(),

    // Creación de sesiones
    val showCreateSessionDialog: Boolean = false,
    val creationMode: SessionCreationMode = SessionCreationMode.Single,
    val singleSessionDate: LocalDate = LocalDate.now(),
    val rangeStartDate: LocalDate = LocalDate.now(),
    val rangeEndDate: LocalDate = LocalDate.now(),
    val selectedWeekdays: Set<DayOfWeek> = emptySet(),
    val startTimeText: String = "10:00", // HH:mm
    val endTimeText: String = "11:00",   // HH:mm
    val creationError: String? = null,
    val creationSuccess: String? = null,
    val isCreatingSessions: Boolean = false,

    // Pickers de creación de sesiones
    val showSingleDatePicker: Boolean = false,
    val showRangeStartPicker: Boolean = false,
    val showRangeEndPicker: Boolean = false,

    // Picker de fecha principal (ver asistencia)
    val showSelectedDatePicker: Boolean = false,

    // Vista de sesiones
    val showAllSessions: Boolean = false, // Nueva: mostrar todas las sesiones en lista vertical

    // Actualización de asistencia
    val isUpdatingAttendance: Boolean = false,
    val attendanceError: String? = null,
    val attendanceMessage: String? = null
)

// Modo de creación de sesiones
enum class SessionCreationMode { Single, Recurring }
