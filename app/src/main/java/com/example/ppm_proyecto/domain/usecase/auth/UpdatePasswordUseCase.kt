package com.example.ppm_proyecto.domain.usecase.auth

import javax.inject.Inject
import com.example.ppm_proyecto.domain.repository.auth.AuthRepository
import com.example.ppm_proyecto.core.util.Result

class UpdatePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String): Result<Unit> =
        authRepository.updatePassword(newPassword)
}