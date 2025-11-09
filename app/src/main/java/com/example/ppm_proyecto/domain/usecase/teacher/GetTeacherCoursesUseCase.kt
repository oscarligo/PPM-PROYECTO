package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class GetTeacherCoursesUseCase (private val repository: TeacherRepository) {
    suspend operator fun invoke(teacherId: String): Result<List<Course>> =
        try { Result.Ok(repository.getCourses(teacherId)) }
        catch (t: Throwable) { Result.Err(t) }
}

