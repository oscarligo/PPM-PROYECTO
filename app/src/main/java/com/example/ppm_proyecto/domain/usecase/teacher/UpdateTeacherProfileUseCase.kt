package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository

class UpdateTeacherProfileUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(user: User): Result<Boolean> =
        try { Result.Ok(repository.updateTeacherProfile(user)) } catch (t: Throwable) { Result.Err(t) }
}

