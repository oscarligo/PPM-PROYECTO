package com.example.ppm_proyecto.domain.usecase.user

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.user.UserRepository

class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User): Result<Unit> = repository.createUser(user)
}

