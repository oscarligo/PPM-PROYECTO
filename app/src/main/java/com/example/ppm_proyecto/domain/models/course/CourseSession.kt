package com.example.ppm_proyecto.domain.models.course

import com.google.firebase.Timestamp



/**
 * Clase que representa una sesión de un curso
 */

data class CourseSession(
    val id: String = "",
    val scheduledDate: Timestamp? = Timestamp.now(),
    val startTime: Timestamp? = Timestamp.now(),
    val endTime: Timestamp? = Timestamp.now(),
    val topic: String = ""
)