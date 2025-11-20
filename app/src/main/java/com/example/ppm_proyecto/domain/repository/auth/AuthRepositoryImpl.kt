package com.example.ppm_proyecto.domain.repository.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.tasks.await
import com.example.ppm_proyecto.core.util.Result

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.Ok(Unit)
        } catch (t: Throwable) {
            Result.Err(t)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Ok(Unit)
        } catch (t: Throwable) {
            Result.Err(t)
        }
    }

    override suspend fun currentUser(): String? = firebaseAuth.currentUser?.uid

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    // Función para la reautenticación
    override suspend fun reauthenticate(email: String, password: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.Err(IllegalStateException("No authenticated user"))
        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential).await()
            Result.Ok(Unit)
        } catch (t: Throwable) {
            Result.Err(t)
        }
    }

    // Función para actualizar email
    override suspend fun updateEmail(newEmail: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.Err(IllegalStateException("No authenticated user"))
        return try {
            user.updateEmail(newEmail).await()
            Result.Ok(Unit)
        } catch (t: Throwable) {
            Result.Err(t)
        }
    }

    // Para actualizar contraseña
    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.Err(IllegalStateException("No authenticated user"))
        return try {
            user.updatePassword(newPassword).await()
            Result.Ok(Unit)
        } catch (t: Throwable) {
            Result.Err(t)
        }
    }


}
