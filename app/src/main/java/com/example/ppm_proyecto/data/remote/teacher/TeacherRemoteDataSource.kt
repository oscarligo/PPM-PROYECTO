package com.example.ppm_proyecto.data.remote.teacher

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Todas las operaciones directas contra Firestore para profesores.
 */
class TeacherRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    // Obtiene la lista de cursos asignados a un profesor
    // Su usecase es GetTeacherCoursesUseCase
    suspend fun fetchTeacherCourses(teacherId: String): List<Course> {
        val snap = db.collection("courses")
            .whereEqualTo("teacherId", teacherId)
            .get()
            .await()
        return snap.toObjects(Course::class.java)
    }

    // Obtiene las sesiones de un curso específico
    // Su usecase es GetSessions
    suspend fun fetchSessions(courseId: String): List<CourseSession> {
        val snap = db.collection("courses")
            .document(courseId)
            .collection("sessions")
            .get()
            .await()
        return snap.toObjects(CourseSession::class.java)
    }

    // Crea un nuevo curso
    // Su usecase es CreateCourseUseCase
    suspend fun createCourse(course: Course): Boolean = try {
        val id = course.id.ifEmpty { db.collection("courses").document().id }
        db.collection("courses").document(id).set(course.copy(id = id)).await()
        true
    } catch (_: Exception) { false }


    // Actualiza un curso existente
    suspend fun updateCourse(course: Course): Boolean = try {
        require(course.id.isNotEmpty()) { "Course id is required" }
        db.collection("courses").document(course.id).set(course).await()
        true
    } catch (_: Exception) { false }


    // Elimina un curso

    suspend fun deleteCourse(courseId: String): Boolean = try {
        db.collection("courses").document(courseId).delete().await()
        true
    } catch (_: Exception) { false }

    suspend fun createSession(courseId: String, session: CourseSession): Boolean = try {
        val id = session.id.ifEmpty { db.collection("courses").document(courseId).collection("sessions").document().id }
        db.collection("courses").document(courseId).collection("sessions").document(id).set(session.copy(id = id)).await()
        true
    } catch (_: Exception) { false }


    // Actualiza una sesión existente
    suspend fun updateSession(courseId: String, session: CourseSession): Boolean = try {
        require(session.id.isNotEmpty()) { "Session id is required" }
        db.collection("courses").document(courseId).collection("sessions").document(session.id).set(session).await()
        true
    } catch (_: Exception) { false }


    // Elimina una sesión

    suspend fun deleteSession(courseId: String, sessionId: String): Boolean = try {
        db.collection("courses").document(courseId).collection("sessions").document(sessionId).delete().await()
        true
    } catch (_: Exception) { false }


    // Marca la asistencia de un estudiante para una sesión específica

    suspend fun markStudentAttendance(courseId: String, sessionId: String, studentId: String, status: String): Boolean = try {
        val att = SessionAttendance(
            id = sessionId,
            studentId = studentId,
            courseId = courseId,
            sessionId = sessionId,
            status = AttendanceStatus.valueOf(status),
            entryTime = com.google.firebase.Timestamp.now()
        )
        db.collection("users").document(studentId).collection("attendance").document(sessionId).set(att).await()
        true
    } catch (_: Exception) { false }

    // Obtiene la lista de asistencias para una sesión específica

    suspend fun getSessionAttendance(courseId: String, sessionId: String): List<SessionAttendance> {
        // Collection group query sobre todas las subcolecciones "attendance"
        val query = db.collectionGroup("attendance")
            .whereEqualTo(FieldPath.of("courseId"), courseId)
            .whereEqualTo(FieldPath.of("sessionId"), sessionId)
        val snap = query.get().await()
        return snap.toObjects(SessionAttendance::class.java)
    }

    // Obtiene la lista de estudiantes inscritos en un curso específico
    suspend fun getCourseStudents(courseId: String): List<User> {
        // Estrategia alternativa: obtener enrollments sin collectionGroup
        // En lugar de usar collectionGroup, buscaremos directamente en la estructura

        // Primero, obtenemos todos los usuarios
        val allUsers = db.collection("users").get().await()
        val result = mutableListOf<User>()

        // Para cada usuario, verificamos si tiene un enrollment para este curso
        for (userDoc in allUsers.documents) {
            val userId = userDoc.id
            val enrollmentExists = db.collection("users")
                .document(userId)
                .collection("enrollments")
                .document(courseId)
                .get()
                .await()
                .exists()

            if (enrollmentExists) {
                userDoc.toObject(User::class.java)?.let { user ->
                    result += user.copy(id = userId)
                }
            }
        }

        return result
    }

    // Marcar asistencia buscando profesor por tag NFC y sesión activa
    suspend fun markAttendanceViaTeacherTag(nfcTagId: String, studentId: String, status: String): Boolean {
        return try {
            if (nfcTagId.isBlank() || studentId.isBlank()) return false

            // Buscar profesor con ese tag
            val teacherSnap = db.collection("users")
                .whereEqualTo("nfcTagId", nfcTagId)
                .get()
                .await()

            val teacherDoc = teacherSnap.documents.firstOrNull { doc ->
                doc.getString("role") == UserRole.Teacher.name
            } ?: return false

            val teacherId = teacherDoc.id

            // Obtener cursos del profesor
            val courses = fetchTeacherCourses(teacherId)
            if (courses.isEmpty()) return false

            val now = Timestamp.now().toDate()
            var targetCourseId: String? = null
            var targetSessionId: String? = null

            // Buscar sesión activa dentro de [startTime, endTime]
            loop@ for (course in courses) {
                val sessions = fetchSessions(course.id)
                for (s in sessions) {
                    val start = s.startTime?.toDate()
                    val end = s.endTime?.toDate()
                    if (start != null && end != null && now.after(start) && now.before(end)) {
                        targetCourseId = course.id
                        targetSessionId = s.id
                        break@loop
                    }
                }
            }

            if (targetCourseId == null || targetSessionId == null) return false

            markStudentAttendance(targetCourseId!!, targetSessionId!!, studentId, status)
        } catch (_: Exception) { false }
    }
}
