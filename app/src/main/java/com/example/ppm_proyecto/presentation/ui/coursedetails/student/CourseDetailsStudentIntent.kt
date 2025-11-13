package com.example.ppm_proyecto.presentation.ui.coursedetails.student

/**
 * Intenciones/acciones que puede realizar el usuario en la pantalla de detalles del curso
 */
sealed interface CourseDetailsStudentIntent {
    data class LoadCourseDetails(val courseId: String, val studentId: String) : CourseDetailsStudentIntent
    object RetryLoading : CourseDetailsStudentIntent
    object ClearError : CourseDetailsStudentIntent
}