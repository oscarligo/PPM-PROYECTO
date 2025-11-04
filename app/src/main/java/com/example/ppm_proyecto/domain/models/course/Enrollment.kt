package com.example.ppm_proyecto.domain.models.course



/**
 * Clase que representa la inscripción de un estudiante en un curso.
 */

data class Enrollment(
    val id: String = "",
    val studentId: String = "",
    val courseId: String = "",
)

