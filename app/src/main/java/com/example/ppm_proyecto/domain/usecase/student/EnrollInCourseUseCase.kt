package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.student.StudentRepository

class EnrollInCourseUseCase (private val repository: StudentRepository) {
    suspend operator fun invoke(studentId: String, courseId: String): Result<Boolean> =
        try { Result.Ok(repository.enrollInCourse(studentId, courseId)) }
        catch (t: Throwable) { Result.Err(t) }
}

