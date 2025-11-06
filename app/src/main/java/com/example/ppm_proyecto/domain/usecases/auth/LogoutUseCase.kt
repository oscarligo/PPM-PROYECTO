package com.example.ppm_proyecto.domain.usecases.auth

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.auth.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke()  = repository.logout()
}

