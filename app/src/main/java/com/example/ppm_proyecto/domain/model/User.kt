package com.example.ppm_proyecto.domain.model

enum class UserRole { Student, Teacher }

data class User(
    val id: String,
    val name: String,
    val role: UserRole,
    val profileImageUrl: String = ""
)

