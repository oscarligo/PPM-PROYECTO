package com.example.ppm_proyecto.data.remote.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Todas las operaciones directas contra Firestore para estudiantes.
 */
class StudentRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    // Obtiene la lista de cursos en los que un estudiante está inscrito
    suspend fun fetchStudentCourses(studentId: String): List<Course> {
        val enrollmentSnapshots = db.collection("users")
            .document(studentId)
            .collection("enrollments")
            .get()
            .await()

        val courseIds = enrollmentSnapshots.documents.map { it.id }
        if (courseIds.isEmpty()) return emptyList()

        val chunks = courseIds.chunked(10)
        val results = mutableListOf<Course>()
        for (chunk in chunks) {
            val snapshot = db.collection("courses")
                .whereIn("id", chunk)
                .get()
                .await()
            results += snapshot.toObjects(Course::class.java)
        }
        return results
    }

    // Obtiene las sesiones de un curso específico
    suspend fun fetchCourseSessions(courseId: String): List<CourseSession> {
        val snapshot = db.collection("courses")
            .document(courseId)
            .collection("sessions")
            .get()
            .await()
        return snapshot.toObjects(CourseSession::class.java)
    }

    // Obtiene los registros de asistencia de un estudiante para un curso específico
    suspend fun fetchStudentAttendance(courseId: String, studentId: String): List<SessionAttendance> {
        val snapshot = db.collection("users")
            .document(studentId)
            .collection("attendance")
            .whereEqualTo("courseId", courseId)
            .get()
            .await()
        return snapshot.toObjects(SessionAttendance::class.java)
    }

    // Inscribe a un estudiante en un curso
    suspend fun enroll(studentId: String, courseId: String): Boolean = try {
        val enrollmentData = mapOf("courseId" to courseId)
        db.collection("users")
            .document(studentId)
            .collection("enrollments")
            .document(courseId)
            .set(enrollmentData)
            .await()
        true
    } catch (_: Exception) { false }

    // Elimina la inscripción de un estudiante en un curso
    suspend fun drop(studentId: String, courseId: String): Boolean = try {
        db.collection("users")
            .document(studentId)
            .collection("enrollments")
            .document(courseId)
            .delete()
            .await()
        true
    } catch (_: Exception) { false }

    // Marca la asistencia de un estudiante para una sesión específica
    suspend fun markAttendance(
        studentId: String,
        courseId: String,
        sessionId: String,
        status: String
    ): Boolean = try {
        val attendance = SessionAttendance(
            id = sessionId,
            studentId = studentId,
            courseId = courseId,
            sessionId = sessionId,
            status = status,
            entryTime = com.google.firebase.Timestamp.now()
        )
        db.collection("users")
            .document(studentId)
            .collection("attendance")
            .document(sessionId)
            .set(attendance)
            .await()
        true
    } catch (_: Exception) { false }
}
