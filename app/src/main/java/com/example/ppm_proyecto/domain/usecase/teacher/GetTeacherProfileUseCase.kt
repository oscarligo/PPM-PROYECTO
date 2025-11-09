package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class GetTeacherProfileUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(teacherId: String): Result<User?> =
        try { Result.Ok(repository.getTeacher(teacherId)) } catch (t: Throwable) { Result.Err(t) }
}

