package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class GetCourseSessionsForTeacherUseCase (private val repository: TeacherRepository) {
    suspend operator fun invoke(courseId: String): Result<List<CourseSession>> =
        try { Result.Ok(repository.getSessionsForCourse(courseId)) }
        catch (t: Throwable) { Result.Err(t) }
}

