package com.example.ppm_proyecto.data.remote.users

import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.ppm_proyecto.core.util.Result

/**
 * Operaciones directas a Firestore para la colección de usuarios
 */
class UsersDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun fetchUser(userId: String): User? =
        db.collection("users").document(userId).get().await().toObject(User::class.java)

    suspend fun createUser(user: User): Result<Unit> = try {
        db.collection("users").document(user.id).set(user).await()
        Result.Ok(Unit)
    } catch (t: Throwable) { Result.Err(t) }

    suspend fun fetchUserNotifications(userId: String): List<Notification> = try {
        db.collection("users")
            .document(userId)
            .collection("notifications")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Notification::class.java)
    } catch (_: Throwable) { emptyList() }

    suspend fun updateUserProfile(userId: String, name: String, profileImageUrl: String): Result<Unit> = try {
        val updatedData = mapOf(
            "name" to name,
            "profileImageUrl" to profileImageUrl
        )
        db.collection("users").document(userId).update(updatedData).await()
        Result.Ok(Unit)
    } catch (t: Throwable) { Result.Err(t) }

    suspend fun updateUserEmail(userId: String, newEmail: String): Result<Unit> = try {
        db.collection("users").document(userId)
            .update("email", newEmail).await()
        Result.Ok(Unit)
    } catch (t: Throwable) { Result.Err(t) }

}