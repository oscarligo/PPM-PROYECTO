package com.example.ppm_proyecto.domain.repository.auth

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.User

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<Unit>

    suspend fun login(email: String, password: String): Result<Unit>
    fun currentUser(): String?
    fun logout()
}
