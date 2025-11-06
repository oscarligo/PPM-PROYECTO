package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.core.util.Result



class GetStudentAttendanceUseCase (private val repository: StudentRepository) {
    suspend operator fun invoke(courseId: String, studentId: String): Result<List<SessionAttendance>> =
        try { Result.Ok(repository.getAttendance(courseId, studentId)) }
        catch (t: Throwable) { Result.Err(t) }

}