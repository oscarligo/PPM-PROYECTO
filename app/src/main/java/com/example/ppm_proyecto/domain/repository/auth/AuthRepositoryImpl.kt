package com.example.ppm_proyecto.domain.repository.auth
import com.google.firebase.auth.FirebaseAuth
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

    override suspend fun logout():  Unit {
        firebaseAuth.signOut()
    }
}
