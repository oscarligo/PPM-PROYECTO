package com.example.ppm_proyecto.domain.usecase.auth

import javax.inject.Inject
import com.example.ppm_proyecto.domain.repository.auth.AuthRepository
import com.example.ppm_proyecto.core.util.Result

class ReauthenticateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.reauthenticate(email, password)
}
