package com.example.ppm_proyecto.presentation.ui.settings.security

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.auth.CurrentUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.auth.ReauthenticateUseCase
import com.example.ppm_proyecto.domain.usecase.auth.UpdateEmailUseCase
import com.example.ppm_proyecto.domain.usecase.auth.UpdatePasswordUseCase
import com.example.ppm_proyecto.presentation.ui.settings.security.SecurityContract.SecuritySettingsState
import com.example.ppm_proyecto.presentation.ui.settings.security.SecurityContract.SecuritySettingsIntent

@HiltViewModel
class SecuritySettingsViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    var state by mutableStateOf(SecuritySettingsState())
        private set

    private var userId: String? = null

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userId = currentUserUseCase()
            userId?.let { id ->
                when (val result = getUserUseCase(id)) {
                    is Result.Ok -> state = state.copy(currentEmail = result.value?.email ?: "")
                    is Result.Err -> {} // ignore
                }
            }
        }
    }

    fun onIntent(intent: SecuritySettingsIntent) {
        when (intent) {
            is SecuritySettingsIntent.ChangeNewEmail ->
                state = state.copy(newEmail = intent.value, errorMessage = null, successMessage = null)

            is SecuritySettingsIntent.ChangeOldPassword ->
                state = state.copy(oldPassword = intent.value, errorMessage = null, successMessage = null)

            is SecuritySettingsIntent.ChangeNewPassword ->
                state = state.copy(newPassword = intent.value, errorMessage = null, successMessage = null)

            is SecuritySettingsIntent.ChangeConfirmPassword ->
                state = state.copy(confirmPassword = intent.value, errorMessage = null, successMessage = null)

            SecuritySettingsIntent.SubmitEmailChange ->
                changeEmail()

            SecuritySettingsIntent.SubmitPasswordChange ->
                changePassword()
        }
    }

    private fun changeEmail() {
        val userId = userId ?: return
        val email = state.currentEmail
        val pass = state.oldPassword
        val newEmail = state.newEmail

        if (newEmail.isBlank()) {
            state = state.copy(errorMessage = "El correo no puede estar vacío")
            return
        }

        if (pass.isBlank()) {
            state = state.copy(errorMessage = "Ingresa tu contraseña actual")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null, successMessage = null)

            // 1. Reautenticar
            when (val reauthRes = reauthenticateUseCase(email, pass)) {
                is Result.Err -> {
                    val msg = reauthRes.throwable.message ?: "Error al reautenticar"
                    state = state.copy(
                        isLoading = false,
                        errorMessage = msg
                    )
                    return@launch
                }
                else -> {}
            }

            // 2. Actualizar email
            when (val updateRes = updateEmailUseCase(userId, newEmail)) {
                is Result.Ok -> state = state.copy(
                    isLoading = false,
                    successMessage = "Correo actualizado exitosamente",
                    currentEmail = newEmail,
                    newEmail = "",
                    oldPassword = ""
                )
                is Result.Err -> state = state.copy(
                    isLoading = false,
                    errorMessage = updateRes.throwable.message ?: "Error al actualizar el correo"
                )
            }
        }
    }

    private fun changePassword() {
        if (state.newPassword.isBlank()) {
            state = state.copy(errorMessage = "La contraseña no puede estar vacía")
            return
        }

        if (state.newPassword.length < 6) {
            state = state.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (state.newPassword != state.confirmPassword) {
            state = state.copy(errorMessage = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null, successMessage = null)

            when (val pwRes = updatePasswordUseCase(state.newPassword)) {
                is Result.Ok -> state = state.copy(
                    isLoading = false,
                    successMessage = "Contraseña actualizada exitosamente",
                    newPassword = "",
                    confirmPassword = ""
                )
                is Result.Err -> state = state.copy(
                    isLoading = false,
                    errorMessage = pwRes.throwable.message ?: "Error al actualizar contraseña"
                )
            }
        }
    }
}
