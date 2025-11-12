package com.example.ppm_proyecto.domain.repository.user

import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.Notification
import com.example.ppm_proyecto.core.util.Result

interface UserRepository {
    suspend fun getUser(userId: String): User?
    // Nuevo: crea o actualiza el documento del usuario
    suspend fun createUser(user: User): Result<Unit>
    // Nuevo: obtener las notificaciones del usuario
    suspend fun getUserNotifications(userId: String): List<Notification>
    // Actualizar perfil del usuario (solo nombre y foto)
    suspend fun updateUserProfile(userId: String, name: String, profileImageUrl: String): Result<Unit>
}
