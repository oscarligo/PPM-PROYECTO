package com.example.ppm_proyecto.domain.repository.teacher

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession


interface TeacherRepository {
    suspend fun getCourses(teacherId: String): List<Course>
    suspend fun getSessionsForCourse(courseId: String): List<CourseSession>

}