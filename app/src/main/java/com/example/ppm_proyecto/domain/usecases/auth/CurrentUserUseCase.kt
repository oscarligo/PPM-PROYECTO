package com.example.ppm_proyecto.domain.usecases.auth

import com.example.ppm_proyecto.domain.repository.auth.AuthRepository

class CurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): String? = repository.currentUser()
}

