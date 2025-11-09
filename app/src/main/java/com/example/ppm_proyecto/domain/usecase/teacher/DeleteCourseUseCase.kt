package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class DeleteCourseUseCase (private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String): Result<Boolean> =
        try { Result.Ok(repository.deleteCourse(courseId)) }
        catch (t: Throwable) { Result.Err(t) }
}

