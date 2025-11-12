package com.example.ppm_proyecto.domain.usecase.user

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.user.UserRepository

class GetUserUseCase (private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<User?> =
        try { Result.Ok(repository.getUser(userId)) }
        catch (t: Throwable) { Result.Err(t) }
}
