package com.example.ppm_proyecto.domain.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance


interface StudentRepository {

    suspend fun getCourses(studentId: String): List<Course> // Obtener lista de cursos en los que está inscrito el estudiante

    suspend fun getSessions(courseId: String): List<CourseSession> // Obtener sesiones de un curso específico

    suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> // Obtener registros de asistencia del estudiante para un curso específico

    suspend fun enrollInCourse(studentId: String, courseId: String): Boolean

    suspend fun dropCourse(studentId: String, courseId: String): Boolean

    suspend fun markAttendance (studentId: String, courseId: String, sessionId: String, status: String): Boolean


}
