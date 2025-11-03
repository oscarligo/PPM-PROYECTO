package com.example.ppm_proyecto.domain.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.user.User

interface StudentRepository {
    suspend fun getStudentData(studentId: String): Result<User?>
    suspend fun getCourses(studentId: String): Result<List<Course>>
    suspend fun getAttendance(courseId: String): Result<List<CourseSession>>

}