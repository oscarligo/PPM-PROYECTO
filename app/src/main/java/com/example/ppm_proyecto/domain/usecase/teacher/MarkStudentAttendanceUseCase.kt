package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

/**
 * Marca la asistencia de un estudiante en una sesión desde el contexto del profesor.
 */
class MarkStudentAttendanceUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String, sessionId: String, studentId: String, status: String): Result<Boolean> =
        try { Result.Ok(repository.markStudentAttendance(courseId, sessionId, studentId, status)) }
        catch (t: Throwable) { Result.Err(t) }
}

