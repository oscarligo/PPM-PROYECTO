package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.student.StudentRepository

class IsStudentEnrolledInCourseUseCase(private val repository: StudentRepository) {
    suspend operator fun invoke(studentId: String, courseId: String): Result<Boolean> =
        try {
            val courses = repository.getCourses(studentId)
            Result.Ok(courses.any { it.id == courseId })
        } catch (t: Throwable) { Result.Err(t) }
}

