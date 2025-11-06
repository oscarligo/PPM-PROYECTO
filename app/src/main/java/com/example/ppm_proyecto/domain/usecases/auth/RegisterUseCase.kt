package com.example.ppm_proyecto.domain.usecases.auth

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.auth.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.register(email, password)
}
