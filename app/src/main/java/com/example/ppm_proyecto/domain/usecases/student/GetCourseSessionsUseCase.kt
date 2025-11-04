package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.core.util.Result

class GetCourseSessionsUseCase (
    private val repository: StudentRepository
) {
    suspend operator fun invoke(courseId: String): Result<List<CourseSession>> =
        try { Result.Ok(repository.getSessions(courseId)) }
        catch (t: Throwable) { Result.Err(t) }
}

