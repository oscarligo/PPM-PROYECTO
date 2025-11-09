package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.repository.student.StudentRepository

class GetStudentSessionAttendanceUseCase(private val repository: StudentRepository) {
    suspend operator fun invoke(studentId: String, courseId: String, sessionId: String): Result<SessionAttendance?> =
        try {
            val all = repository.getAttendance(courseId, studentId)
            Result.Ok(all.firstOrNull { it.sessionId == sessionId })
        } catch (t: Throwable) { Result.Err(t) }
}

