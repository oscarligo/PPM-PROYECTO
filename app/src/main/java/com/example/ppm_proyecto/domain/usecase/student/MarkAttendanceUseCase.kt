package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.student.StudentRepository

class MarkAttendanceUseCase (private val repository: StudentRepository) {
    suspend operator fun invoke(studentId: String, courseId: String, sessionId: String, status: String): Result<Boolean> =
        try { Result.Ok(repository.markAttendance(studentId, courseId, sessionId, status)) }
        catch (t: Throwable) { Result.Err(t) }
}

