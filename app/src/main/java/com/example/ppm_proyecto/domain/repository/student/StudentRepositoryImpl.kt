package com.example.ppm_proyecto.domain.repository.student

import com.example.ppm_proyecto.data.remote.student.StudentRemoteDataSource
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import javax.inject.Inject

/**
Implementación de StudentRepository delegando en un DataSource remoto (Firestore).
*/
class StudentRepositoryImpl @Inject constructor(
    private val remote: StudentRemoteDataSource
) : StudentRepository {

    override suspend fun getCourses(studentId: String): List<Course> =
        remote.fetchStudentCourses(studentId)

    override suspend fun getSessions(courseId: String): List<CourseSession> =
        remote.fetchCourseSessions(courseId)

    override suspend fun getAttendance(courseId: String, studentId: String): List<SessionAttendance> =
        remote.fetchStudentAttendance(courseId, studentId)

    override suspend fun enrollInCourse(studentId: String, courseId: String): Boolean =
        remote.enroll(studentId, courseId)

    override suspend fun dropCourse(studentId: String, courseId: String): Boolean =
        remote.drop(studentId, courseId)

    override suspend fun markAttendance(studentId: String, courseId: String, sessionId: String, status: String): Boolean =
        remote.markAttendance(studentId, courseId, sessionId, status)
}
