package com.example.ppm_proyecto.presentation.ui.register

import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.UserRole

object RegisterContract {
    sealed interface Intent {
        data class SetName(val value: String) : Intent
        data class SetEmail(val value: String) : Intent
        data class SetPassword(val value: String) : Intent
        data class SetRole(val value: UserRole) : Intent
        data object Submit : Intent
        data object GoToLogin : Intent
    }

    data class State(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val role: UserRole = UserRole.Student,
        val isLoading: Boolean = false,
        val error: String = "",
        val user: User? = null,
    )

    sealed interface Effect
}

