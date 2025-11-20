package com.example.ppm_proyecto.domain.usecase.auth

import javax.inject.Inject
import com.example.ppm_proyecto.domain.repository.auth.AuthRepository
import com.example.ppm_proyecto.domain.repository.user.UserRepository
import com.example.ppm_proyecto.core.util.Result

class UpdateEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, newEmail: String): Result<Unit> {
        // 1. Firebase Auth
        val authResult = authRepository.updateEmail(newEmail)
        if (authResult is Result.Err) return authResult

        // 2. Firestore
        return userRepository.updateUserEmail(userId, newEmail)
    }
}
