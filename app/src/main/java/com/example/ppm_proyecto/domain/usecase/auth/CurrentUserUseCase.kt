package com.example.ppm_proyecto.domain.usecase.auth

import com.example.ppm_proyecto.domain.repository.auth.AuthRepository

class CurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): String? = repository.currentUser()
}

