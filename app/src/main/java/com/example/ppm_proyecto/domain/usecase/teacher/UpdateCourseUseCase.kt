package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class UpdateCourseUseCase (private val repository: TeacherRepository) {
    suspend operator fun invoke(course: Course): Result<Boolean> =
        try { Result.Ok(repository.updateCourse(course)) }
        catch (t: Throwable) { Result.Err(t) }
}

