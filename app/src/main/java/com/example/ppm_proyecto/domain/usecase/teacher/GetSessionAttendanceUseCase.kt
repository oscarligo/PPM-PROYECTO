package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

/**
 * Obtiene todas las asistencias registradas de una sesión específica.
 */
class GetSessionAttendanceUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String, sessionId: String): Result<List<SessionAttendance>> =
        try { Result.Ok(repository.getSessionAttendance(courseId, sessionId)) }
        catch (t: Throwable) { Result.Err(t) }
}

