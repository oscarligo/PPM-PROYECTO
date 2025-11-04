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
import com.example.ppm_proyecto.data.local.sample.sampleSessionAttendances
import javax.inject.Inject

/*===========================================================================
Implementación de StudentRepository para datos locales en el paquete sample.
=============================================================================*/
class StudentMockRepositoryImpl @Inject constructor() : StudentRepository {

    override suspend fun getStudentData(studentId: String): User? {
        return sampleStudentUsers.find { it.id == studentId }
    }

    override suspend fun getCourses(studentId: String): List<Course> {
        return sampleCourses.filter { course ->
            sampleEnrollments.any { it.courseId == course.id && it.studentId == studentId }
        }
    }

    override suspend fun getSessions(courseId: String): List<CourseSession> {
        val allSessions = sampleMobileSessions + sampleAlgebraSessions
        return allSessions.filter { it.courseId == courseId }
    }

    override suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> {
        val allSessions = sampleMobileSessions + sampleAlgebraSessions
        val sessionIdsOfCourse = allSessions.filter { it.courseId == courseId }.map { it.id }

        // Unir las asistencias de todas las sesiones del curso y filtrar por estudiante
        return sessionIdsOfCourse.flatMap { sessionId ->
            val attendances = sampleSessionAttendances[sessionId].orEmpty()
            attendances.filter { it.studentId == studentId }
        }
    }
}
