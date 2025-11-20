package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import java.time.LocalDate
import java.time.DayOfWeek
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus

// Acciones (intents) que el usuario puede realizar en esta pantalla.
// El ViewModel las "recibe" y decide cómo reaccionar.
sealed class CourseDetailsTeacherIntent {
    data class LoadCourse(val courseId: String) : CourseDetailsTeacherIntent() // Cargar curso y estudiantes desde Firebase
    data class SelectDate(val date: LocalDate) : CourseDetailsTeacherIntent()  // Cambiar fecha seleccionada
    object Refresh : CourseDetailsTeacherIntent()                              // Recargar asistencia o datos
    // Creación de sesiones
    object OpenCreateSessionDialog : CourseDetailsTeacherIntent()
    object CloseCreateSessionDialog : CourseDetailsTeacherIntent()
    data class SetCreationMode(val mode: SessionCreationMode) : CourseDetailsTeacherIntent()
    data class SetSingleSessionDate(val date: LocalDate) : CourseDetailsTeacherIntent()
    data class SetRangeStartDate(val date: LocalDate) : CourseDetailsTeacherIntent()
    data class SetRangeEndDate(val date: LocalDate) : CourseDetailsTeacherIntent()
    data class ToggleWeekday(val day: DayOfWeek) : CourseDetailsTeacherIntent()
    data class SetStartTime(val value: String) : CourseDetailsTeacherIntent()
    data class SetEndTime(val value: String) : CourseDetailsTeacherIntent()
    object SubmitCreateSessions : CourseDetailsTeacherIntent()
    object ClearCreationFeedback : CourseDetailsTeacherIntent()
    // Pickers de fecha (visibilidad)
    object ShowSingleDatePicker : CourseDetailsTeacherIntent()
    object HideSingleDatePicker : CourseDetailsTeacherIntent()
    object ShowRangeStartPicker : CourseDetailsTeacherIntent()
    object HideRangeStartPicker : CourseDetailsTeacherIntent()
    object ShowRangeEndPicker : CourseDetailsTeacherIntent()
    object HideRangeEndPicker : CourseDetailsTeacherIntent()
    // Picker principal de fecha (asistencia)
    object ShowSelectedDatePicker : CourseDetailsTeacherIntent()
    object HideSelectedDatePicker : CourseDetailsTeacherIntent()
    // Vista de sesiones
    object ToggleShowAllSessions : CourseDetailsTeacherIntent() // Nueva: alternar vista de todas las sesiones
    // Nuevos: sesiones y asistencia
    data class SelectSession(val sessionId: String) : CourseDetailsTeacherIntent()
    data class ChangeAttendance(val studentId: String, val status: AttendanceStatus) : CourseDetailsTeacherIntent()
}
