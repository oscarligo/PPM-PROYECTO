package com.example.ppm_proyecto.domain.repository.course

interface CourseRepository {
    suspend fun getStudentCourses(studentId: String): List<String>
}