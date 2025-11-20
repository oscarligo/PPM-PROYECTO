package com.example.ppm_proyecto.domain.repository.user

import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.Notification
import javax.inject.Inject
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.data.remote.users.UsersDataSource

class UserRepositoryImpl @Inject constructor(
    private val remote: UsersDataSource
) : UserRepository {

    override suspend fun getUser(userId: String): User? =
        remote.fetchUser(userId)

    override suspend fun createUser(user: User): Result<Unit> =
        remote.createUser(user)

    override suspend fun getUserNotifications(userId: String): List<Notification> =
        remote.fetchUserNotifications(userId)

    override suspend fun updateUserProfile(userId: String, name: String, profileImageUrl: String): Result<Unit> =
        remote.updateUserProfile(userId, name, profileImageUrl)

    override suspend fun updateUserNfcTag(userId: String, nfcTagId: String): Result<Unit> =
        remote.updateUserNfcTag(userId, nfcTagId)

    override suspend fun updateUserEmail(userId: String, newEmail: String): Result<Unit> =
    remote.updateUserEmail(userId, newEmail)

}
