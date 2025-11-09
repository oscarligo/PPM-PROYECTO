package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class UpdateSessionUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String, session: CourseSession): Result<Boolean> =
        try { Result.Ok(repository.updateSession(courseId, session)) } catch (t: Throwable) { Result.Err(t) }
}

