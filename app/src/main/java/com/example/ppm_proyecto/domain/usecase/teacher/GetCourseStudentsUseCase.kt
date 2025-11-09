package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class GetCourseStudentsUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String): Result<List<User>> =
        try { Result.Ok(repository.getCourseStudents(courseId)) } catch (t: Throwable) { Result.Err(t) }
}

