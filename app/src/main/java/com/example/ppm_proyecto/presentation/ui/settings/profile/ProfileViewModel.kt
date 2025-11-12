package com.example.ppm_proyecto.presentation.ui.settings.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.auth.CurrentUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    var state = mutableStateOf(ProfileContract.State())
        private set

    fun ensureLoaded() {
            loadUserData()
    }

    fun onIntent(intent: ProfileContract.Intent) {
        when (intent) {
            is ProfileContract.Intent.LoadUser -> loadUser(intent.userId)
            is ProfileContract.Intent.SetName -> state.value = state.value.copy(name = intent.value)
            is ProfileContract.Intent.SetProfileImageUrl -> state.value = state.value.copy(profileImageUrl = intent.value)
            ProfileContract.Intent.SaveChanges -> saveChanges()
            is ProfileContract.Intent.CopyToClipboard -> { /* Handled in UI */ }
            ProfileContract.Intent.OpenImageUrlDialog -> {
                state.value = state.value.copy(
                    showImageUrlDialog = true,
                    tempImageUrl = state.value.profileImageUrl
                )
            }
            ProfileContract.Intent.CloseImageUrlDialog -> {
                state.value = state.value.copy(showImageUrlDialog = false)
            }
            is ProfileContract.Intent.SetTempImageUrl -> {
                state.value = state.value.copy(tempImageUrl = intent.value)
            }
            ProfileContract.Intent.SaveImageUrl -> {
                state.value = state.value.copy(
                    profileImageUrl = state.value.tempImageUrl,
                    showImageUrlDialog = false
                )
            }
            ProfileContract.Intent.RefreshUserData -> {
                loadUserData()
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "", successMessage = "")
            val currentUserId = currentUserUseCase()
            if (currentUserId == null) {
                state.value = state.value.copy(
                    isLoading = false,
                    error = "Usuario no autenticado",
                    loaded = true
                )
                return@launch
            }

            when (val result = getUserUseCase(currentUserId)) {
                is Result.Ok -> {
                    val user = result.value
                    if (user != null) {
                        state.value = state.value.copy(
                            user = user,
                            name = user.name,
                            profileImageUrl = user.profileImageUrl,
                            isLoading = false,
                            loaded = true
                        )
                    } else {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = "Usuario no encontrado",
                            loaded = true
                        )
                    }
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = result.throwable.message ?: "Error al cargar el usuario",
                        loaded = true
                    )
                }
            }
        }
    }

    private fun loadUser(userId: String) {
        if (userId.isBlank()) return
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "", successMessage = "")
            when (val result = getUserUseCase(userId)) {
                is Result.Ok -> {
                    val user = result.value
                    if (user != null) {
                        state.value = state.value.copy(
                            user = user,
                            name = user.name,
                            profileImageUrl = user.profileImageUrl,
                            isLoading = false,
                            loaded = true
                        )
                    } else {
                        state.value = state.value.copy(
                            isLoading = false,
                            error = "Usuario no encontrado",
                            loaded = true
                        )
                    }
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = result.throwable.message ?: "Error al cargar el usuario",
                        loaded = true
                    )
                }
            }
        }
    }

    private fun saveChanges() {
        val user = state.value.user ?: return

        if (state.value.name.trim().isEmpty()) {
            state.value = state.value.copy(error = "El nombre no puede estar vacío")
            return
        }

        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "", successMessage = "")

            when (val result = updateUserProfileUseCase(
                userId = user.id,
                name = state.value.name.trim(),
                profileImageUrl = state.value.profileImageUrl.trim()
            )) {
                is Result.Ok -> {
                    val updatedUser = user.copy(
                        name = state.value.name.trim(),
                        profileImageUrl = state.value.profileImageUrl.trim()
                    )
                    state.value = state.value.copy(
                        user = updatedUser,
                        isLoading = false,
                        successMessage = "Perfil actualizado correctamente"
                    )
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = result.throwable.message ?: "Error al actualizar el perfil"
                    )
                }
            }
        }
    }
}
