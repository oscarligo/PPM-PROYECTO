package com.example.ppm_proyecto.domain.repository.auth

import com.example.ppm_proyecto.core.util.Result

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<Unit>

    suspend fun login(email: String, password: String): Result<Unit>
    
    suspend fun currentUser(): String?

    suspend fun logout()

    //Para actualizar datos en Security
    suspend fun reauthenticate(email: String, password: String): Result<Unit>

    suspend fun updateEmail(newEmail: String): Result<Unit>

    suspend fun updatePassword(newPassword: String): Result<Unit>
}
