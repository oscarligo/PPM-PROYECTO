package com.example.ppm_proyecto.data.remote.nfc

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

/**
 * Data source para operaciones NFC con Firestore
 */
class NfcRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    /**
     * Vincula un tag NFC a un usuario (profesor)
     */
    suspend fun linkNfcTagToUser(userId: String, nfcTagId: String) {
        db.collection("users")
            .document(userId)
            .update("nfcTagId", nfcTagId)
            .await()
    }

    /**
     * Busca un profesor por el ID de su tag NFC
     */
    suspend fun findTeacherByNfcTag(nfcTagId: String): String? {
        val snapshot = db.collection("users")
            .whereEqualTo("nfcTagId", nfcTagId)
            .whereEqualTo("role", UserRole.Teacher.name)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.id
    }

    /**
     * Verifica si un tag NFC ya está vinculado a algún usuario
     */
    suspend fun isNfcTagAlreadyLinked(nfcTagId: String): Boolean {
        val snapshot = db.collection("users")
            .whereEqualTo("nfcTagId", nfcTagId)
            .get()
            .await()

        return !snapshot.isEmpty
    }

    /**
     * Marca la asistencia de un estudiante cuando detecta el tag del profesor
     *
     * Proceso:
     * 1. Busca al profesor por el ID del tag
     * 2. Obtiene los cursos del profesor
     * 3. Para cada curso, busca las sesiones
     * 4. Encuentra la sesión activa que coincida con el día y hora actual
     * 5. Marca la asistencia del estudiante en esa sesión
     */
    suspend fun markAttendanceViaTeacherTag(nfcTagId: String, studentId: String): Boolean {
        return try {
            if (nfcTagId.isBlank() || studentId.isBlank()) return false

            // 1. Buscar profesor con ese tag NFC
            val teacherSnap = db.collection("users")
                .whereEqualTo("nfcTagId", nfcTagId)
                .whereEqualTo("role", UserRole.Teacher.name)
                .get()
                .await()

            val teacherId = teacherSnap.documents.firstOrNull()?.id ?: return false

            // 2. Obtener cursos del profesor
            val coursesSnap = db.collection("courses")
                .whereEqualTo("teacherId", teacherId)
                .get()
                .await()

            val courses = coursesSnap.toObjects(Course::class.java)
            if (courses.isEmpty()) return false

            // 3. Buscar sesión activa
            val now = Timestamp.now()
            val nowDate = now.toDate()
            val calendar = Calendar.getInstance().apply { time = nowDate }

            var targetCourseId: String? = null
            var targetSessionId: String? = null

            // Buscar en todos los cursos del profesor
            loop@ for (course in courses) {
                val sessionsSnap = db.collection("courses")
                    .document(course.id)
                    .collection("sessions")
                    .get()
                    .await()

                val sessions = sessionsSnap.toObjects(CourseSession::class.java)

                for (session in sessions) {
                    if (isSessionActive(session, nowDate, calendar)) {
                        targetCourseId = course.id
                        targetSessionId = session.id
                        break@loop
                    }
                }
            }

            // Si no se encontró sesión activa, retornar false
            if (targetCourseId == null || targetSessionId == null) return false

            // 4. Verificar que el estudiante está inscrito en el curso
            val enrollmentExists = db.collection("users")
                .document(studentId)
                .collection("enrollments")
                .document(targetCourseId)
                .get()
                .await()
                .exists()

            if (!enrollmentExists) return false

            // 5. Marcar asistencia del estudiante
            val attendance = SessionAttendance(
                id = targetSessionId,
                studentId = studentId,
                courseId = targetCourseId,
                sessionId = targetSessionId,
                status = AttendanceStatus.Present,
                entryTime = now
            )

            db.collection("users")
                .document(studentId)
                .collection("attendance")
                .document(targetSessionId)
                .set(attendance)
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Verifica si una sesión está activa en este momento
     * Una sesión está activa si:
     * - La fecha programada coincide con el día actual
     * - La hora actual está entre startTime y endTime
     */
    private fun isSessionActive(
        session: CourseSession,
        nowDate: java.util.Date,
        nowCalendar: Calendar
    ): Boolean {
        val scheduledDate = session.scheduledDate?.toDate() ?: return false
        val startTime = session.startTime?.toDate() ?: return false
        val endTime = session.endTime?.toDate() ?: return false

        // Verificar que sea el mismo día
        val scheduledCalendar = Calendar.getInstance().apply { time = scheduledDate }
        val isSameDay = nowCalendar.get(Calendar.YEAR) == scheduledCalendar.get(Calendar.YEAR) &&
                nowCalendar.get(Calendar.DAY_OF_YEAR) == scheduledCalendar.get(Calendar.DAY_OF_YEAR)

        if (!isSameDay) return false

        // Verificar que esté dentro del rango de tiempo
        return nowDate.after(startTime) && nowDate.before(endTime)
    }
}

