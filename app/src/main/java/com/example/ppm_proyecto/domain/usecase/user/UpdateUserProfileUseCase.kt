package com.example.ppm_proyecto.domain.usecase.user

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.user.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, name: String, profileImageUrl: String): Result<Unit> =
        repository.updateUserProfile(userId, name, profileImageUrl)
}

