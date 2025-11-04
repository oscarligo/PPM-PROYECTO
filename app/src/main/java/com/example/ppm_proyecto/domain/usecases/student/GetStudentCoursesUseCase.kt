package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.core.util.Result


class GetStudentCoursesUseCase (
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: String): Result<List<Course>>  =
        try { Result.Ok(repository.getCourses(studentId)) }
        catch (t: Throwable) { Result.Err(t)
    }
}

