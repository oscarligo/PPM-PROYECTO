package com.example.ppm_proyecto.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.example.ppm_proyecto.domain.usecases.auth.LoginUseCase
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.StudentHome
import com.example.ppm_proyecto.presentation.navigation.routes.TeacherHome
import com.example.ppm_proyecto.presentation.ui.home.student.StudentHomeScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    var state = mutableStateOf(LoginContract.State())
        private set

    fun onIntent(intent: LoginContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is LoginContract.Intent.SetEmail -> state.value = state.value.copy(email = intent.value)
            is LoginContract.Intent.SetPassword -> state.value = state.value.copy(password = intent.value)
            LoginContract.Intent.Submit -> submit(navigate)
            LoginContract.Intent.GoToRegister -> navigate(com.example.ppm_proyecto.presentation.navigation.routes.Register)
        }
    }

    private fun submit(navigate: (AppDestination) -> Unit) {
        val email = state.value.email.trim()
        val password = state.value.password

        if (email.isEmpty() || password.isEmpty()) {
            state.value = state.value.copy(error = "Completa todos los campos")
            return
        }

        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = "")
            when (val res = loginUseCase(email, password)) {
                is Result.Ok -> {
                    navigate(StudentHome)
                }
                is Result.Err -> {
                    state.value = state.value.copy(
                        isLoading = false,
                        error = res.throwable.message ?: "Error al iniciar sesión"
                    )
                }
            }
        }
    }
}
