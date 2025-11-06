package com.example.ppm_proyecto.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.example.ppm_proyecto.domain.usecases.auth.RegisterUseCase
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.StudentHome
import com.example.ppm_proyecto.presentation.navigation.routes.TeacherHome
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    var state = mutableStateOf(RegisterContract.State())
        private set

    fun onIntent(intent: RegisterContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is RegisterContract.Intent.SetName -> state.value = state.value.copy(name = intent.value)
            is RegisterContract.Intent.SetEmail -> state.value = state.value.copy(email = intent.value)
            is RegisterContract.Intent.SetPassword -> state.value = state.value.copy(password = intent.value)
            is RegisterContract.Intent.SetRole -> state.value = state.value.copy(role = intent.value)
            RegisterContract.Intent.Submit -> submit(navigate)
            RegisterContract.Intent.GoToLogin -> navigate(com.example.ppm_proyecto.presentation.navigation.routes.Login)
        }
    }

    private fun submit(navigate: (AppDestination) -> Unit) {
        val name = state.value.name.trim()
        val email = state.value.email.trim()
        val password = state.value.password

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            state.value = state.value.copy(error = "Completa todos los campos")
            return
        }

        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "")
            when (val res = registerUseCase( email, password)) {
                is Result.Ok -> {
                    navigate(
                        if (state.value.role == UserRole.Student) {
                            StudentHome
                        } else {
                            TeacherHome
                        }
                    )

                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = res.throwable.message ?: "Error al registrarse"
                    )
                }
            }
        }
    }
}

