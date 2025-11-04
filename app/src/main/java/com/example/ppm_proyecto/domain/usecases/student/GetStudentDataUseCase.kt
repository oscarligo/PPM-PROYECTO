package com.example.ppm_proyecto.domain.usecase.student

import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.core.util.Result

class GetStudentDataUseCase (
    private val repository: StudentRepository
) {
    suspend operator fun invoke(studentId: String): Result<User?> =
        try { Result.Ok(repository.getStudentData(studentId)) }
        catch (t: Throwable) { Result.Err(t) }

}

