package com.example.ppm_proyecto.domain.models.user

import com.google.firebase.Timestamp

enum class UserRole { Student, Teacher }

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.Student,
    val profileImageUrl: String = "",
    val createdAt: Timestamp = Timestamp.now(),
)
