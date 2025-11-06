package com.example.ppm_proyecto.presentation.ui.login

import com.example.ppm_proyecto.domain.models.user.User

object LoginContract {
    sealed interface Intent {
        data class SetEmail(val value: String) : Intent
        data class SetPassword(val value: String) : Intent
        data object Submit : Intent
        data object GoToRegister : Intent
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String = "",
        val user: User? = null,
    )

    sealed interface Effect
}

