package com.example.ppm_proyecto.domain.repository.teacher

import com.example.ppm_proyecto.data.remote.teacher.TeacherRemoteDataSource
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import javax.inject.Inject

class TeacherRepositoryImpl @Inject constructor(
    private val remote: TeacherRemoteDataSource
) : TeacherRepository {
    override suspend fun getTeacher(teacherId: String): User? = remote.getTeacher(teacherId)
    override suspend fun getCourses(teacherId: String): List<Course> = remote.fetchTeacherCourses(teacherId)
    override suspend fun getSessionsForCourse(courseId: String): List<CourseSession> = remote.fetchSessions(courseId)
    override suspend fun createCourse(course: Course): Boolean = remote.createCourse(course)
    override suspend fun updateCourse(course: Course): Boolean = remote.updateCourse(course)
    override suspend fun deleteCourse(courseId: String): Boolean = remote.deleteCourse(courseId)
    override suspend fun createSession(courseId: String, session: CourseSession): Boolean = remote.createSession(courseId, session)
    override suspend fun updateSession(courseId: String, session: CourseSession): Boolean = remote.updateSession(courseId, session)
    override suspend fun deleteSession(courseId: String, sessionId: String): Boolean = remote.deleteSession(courseId, sessionId)
    override suspend fun markStudentAttendance(courseId: String, sessionId: String, studentId: String, status: String): Boolean = remote.markStudentAttendance(courseId, sessionId, studentId, status)
    override suspend fun getSessionAttendance(courseId: String, sessionId: String): List<SessionAttendance> = remote.getSessionAttendance(courseId, sessionId)
    override suspend fun updateTeacherProfile(user: User): Boolean = remote.updateTeacherProfile(user)
    override suspend fun getCourseStudents(courseId: String): List<User> = remote.getCourseStudents(courseId)
}
