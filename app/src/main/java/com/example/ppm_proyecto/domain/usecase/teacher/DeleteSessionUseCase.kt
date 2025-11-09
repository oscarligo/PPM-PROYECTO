package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class DeleteSessionUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String, sessionId: String): Result<Boolean> =
        try { Result.Ok(repository.deleteSession(courseId, sessionId)) } catch (t: Throwable) { Result.Err(t) }
}

