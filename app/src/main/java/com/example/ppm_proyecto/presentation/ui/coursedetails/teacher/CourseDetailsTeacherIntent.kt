package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import java.time.LocalDate

// Acciones (intents) que el usuario puede realizar en esta pantalla.
// El ViewModel las "recibe" y decide cómo reaccionar.
sealed class CourseDetailsTeacherIntent {
    data class LoadCourse(val courseId: String) : CourseDetailsTeacherIntent() // Cargar curso y estudiantes desde Firebase
    data class SelectDate(val date: LocalDate) : CourseDetailsTeacherIntent()  // Cambiar fecha seleccionada
    object Refresh : CourseDetailsTeacherIntent()                              // Recargar asistencia o datos
}
