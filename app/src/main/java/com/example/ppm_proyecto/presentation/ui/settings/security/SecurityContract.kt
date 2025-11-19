package com.example.ppm_proyecto.presentation.ui.settings.security

import com.example.ppm_proyecto.domain.models.user.User

object SecurityContract {
   data class SecuritySettingsState(
        val currentEmail: String = "",
        val oldPassword: String = "",
        val newPassword: String = "",
        val confirmPassword: String = "",
        val newEmail: String = "",
        val isLoading: Boolean = false,
        val successMessage: String? = null,
        val errorMessage: String? = null
    )

    sealed interface SecuritySettingsIntent {
        data class ChangeNewEmail(val value: String) : SecuritySettingsIntent
        data class ChangeOldPassword(val value: String) : SecuritySettingsIntent
        data class ChangeNewPassword(val value: String) : SecuritySettingsIntent
        data class ChangeConfirmPassword(val value: String) : SecuritySettingsIntent
        data object SubmitEmailChange : SecuritySettingsIntent
        data object SubmitPasswordChange : SecuritySettingsIntent
    }

}