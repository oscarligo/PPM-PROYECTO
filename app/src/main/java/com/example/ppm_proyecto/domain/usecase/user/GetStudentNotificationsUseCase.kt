package com.example.ppm_proyecto.domain.usecase.user

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.Notification
import com.example.ppm_proyecto.domain.repository.user.UserRepository

class GetStudentNotificationsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<List<Notification>> =
        try { Result.Ok(repository.getUserNotifications(userId)) }
        catch (t: Throwable) { Result.Err(t) }
}

