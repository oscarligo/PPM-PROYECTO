package com.example.ppm_proyecto.domain.model

data class Course(
    val id: String,
    val name: String,
    val description: String = "",
    val schedule: String = "",
    val students: List<User> = emptyList(),
    val sessions: List<CourseSession> = emptyList(),
    val teacher: User = User(id = "", name = "", role = UserRole.Teacher),
)
