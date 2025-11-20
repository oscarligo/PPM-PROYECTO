package com.example.ppm_proyecto.domain.repository.teacher

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User


interface TeacherRepository {

    suspend fun getCourses(teacherId: String): List<Course>
    suspend fun getSessionsForCourse(courseId: String): List<CourseSession>
    suspend fun createCourse(course: Course): Boolean
    suspend fun updateCourse(course: Course): Boolean
    suspend fun deleteCourse(courseId: String): Boolean

    suspend fun createSession(courseId: String, session: CourseSession): Boolean
    suspend fun updateSession(courseId: String, session: CourseSession): Boolean
    suspend fun deleteSession(courseId: String, sessionId: String): Boolean

    suspend fun markStudentAttendance(courseId: String, sessionId: String, studentId: String, status: String): Boolean
    suspend fun getSessionAttendance(courseId: String, sessionId: String): List<SessionAttendance>

    suspend fun markStudentAttendanceViaTeacherTag(nfcTagId: String, studentId: String, status: String): Boolean


    suspend fun getCourseStudents(courseId: String): List<User>
}