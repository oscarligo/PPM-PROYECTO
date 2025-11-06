package com.example.ppm_proyecto.data.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
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

    override suspend fun getStudentData(studentId: String): User? {
        val document = db.collection("users")
            .document(studentId)
            .get()
            .await()

        return if (document.exists()) document.toObject(User::class.java) else null
    }

    override suspend fun getCourses(studentId: String): List<Course> {
        val snapshot = db.collection("courses")
            .whereArrayContains("studentIds", studentId)
            .get()
            .await()

        return snapshot.toObjects(Course::class.java)
    }

    override suspend fun getSessions(courseId: String): List<CourseSession> {
        val snapshot = db.collection("courseSessions")
            .whereEqualTo("courseId", courseId)
            .get()
            .await()

        return snapshot.toObjects(CourseSession::class.java)
    }

    override suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> {
        // Implementación real pendiente del esquema de Firestore
        return emptyList()
    }
}