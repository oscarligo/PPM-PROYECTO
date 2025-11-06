package com.example.ppm_proyecto.domain.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.data.local.sample.sampleStudentUsers
import com.example.ppm_proyecto.data.local.sample.sampleCourses
import com.example.ppm_proyecto.data.local.sample.sampleEnrollments
import com.example.ppm_proyecto.data.local.sample.sampleMobileSessions
import com.example.ppm_proyecto.data.local.sample.sampleAlgebraSessions
import com.example.ppm_proyecto.data.local.sample.sampleSessionAttendancesBySessionId
import javax.inject.Inject

/*===========================================================================
Implementación de StudentRepository para datos locales en el paquete sample.
=============================================================================*/
class StudentMockRepositoryImpl @Inject constructor( ) : StudentRepository {

    override suspend fun getStudentData(studentId: String): User? {
        return sampleStudentUsers.find { it.id == studentId }
    }

    override suspend fun getCourses(studentId: String): List<Course> {
        return sampleCourses.filter { course ->
            sampleEnrollments.any { it.courseId == course.id && it.studentId == studentId }
        }
    }

    override suspend fun getSessions(courseId: String): List<CourseSession> {
        return when (courseId) {
            "C-MOV" -> sampleMobileSessions
            "C-ALG" -> sampleAlgebraSessions
            else -> emptyList()
        }
    }

    override suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> {
        val sessionIds = when (courseId) {
            "C-MOV" -> sampleMobileSessions.map { it.id }
            "C-ALG" -> sampleAlgebraSessions.map { it.id }
            else -> emptyList()
        }
        return sessionIds.flatMap { sid ->
            sampleSessionAttendancesBySessionId[sid].orElseEmpty().filter { it.studentId == studentId }
        }
    }
}

// Extensión de ayuda para null-safe listas
private fun <T> List<T>?.orElseEmpty(): List<T> = this ?: emptyList()
