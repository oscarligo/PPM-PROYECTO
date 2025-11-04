package com.example.ppm_proyecto.data.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



/*===========================================================================
Implementación del repositorio de estudantes para interactuar con Firestore.
=============================================================================*/

class StudentRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : StudentRepository {

    override suspend fun getStudentData(studentId: String): Result<User?> = runCatching {
        val document = db.collection("users")
            .document(studentId)
            .get()
            .await()

        if (document.exists()) {
            document.toObject(User::class.java)
        } else {
            null
        }
    }

    override suspend fun getCourses(studentId: String): Result<List<Course>> = runCatching {
        val snapshot = db.collection("courses")
            .whereArrayContains("studentIds", studentId)
            .get()
            .await()

        snapshot.toObjects(Course::class.java)

    }

    override suspend fun getAttendance(courseId: String): Result<List<CourseSession>> = runCatching {
        val snapshot = db.collection("courseSessions")
            .whereEqualTo("courseId", courseId)
            .get()
            .await()

        snapshot.toObjects(CourseSession::class.java)
    }
}