package com.example.ppm_proyecto.domain.repository.student

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import kotlinx.coroutines.flow.Flow


interface StudentRepository {

    suspend fun getStudentData(studentId: String): User? // Obtener datos del estudiante por su ID

    suspend fun getCourses(studentId: String): List<Course> // Obtener lista de cursos en los que está inscrito el estudiante

    suspend fun getSessions(courseId: String): List<CourseSession> // Obtener sesiones de un curso específico

    suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> //
}
