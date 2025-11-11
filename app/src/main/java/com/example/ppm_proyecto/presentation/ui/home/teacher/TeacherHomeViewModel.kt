package com.example.ppm_proyecto.presentation.ui.home.teacher

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.usecase.auth.CurrentUserUseCase
import com.example.ppm_proyecto.domain.usecase.auth.LogoutUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetTeacherCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetTeacherProfileUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.presentation.navigation.routes.AppDestination
import com.example.ppm_proyecto.presentation.navigation.routes.CourseDetails
import com.example.ppm_proyecto.presentation.navigation.routes.Login
import com.example.ppm_proyecto.presentation.navigation.routes.Profile
import com.example.ppm_proyecto.presentation.navigation.routes.SecuritySettings
import com.example.ppm_proyecto.presentation.navigation.routes.AppearanceSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que controla la lógica de la pantalla principal del profesor.
 */
@HiltViewModel
class TeacherHomeViewModel @Inject constructor(
    private val getTeacherCoursesUseCase: GetTeacherCoursesUseCase,
    private val getTeacherProfileUseCase: GetTeacherProfileUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    // Estado de la pantalla
    var state = mutableStateOf(TeacherContract.State())
        private set // Para que solo el ViewModel pueda modificarlo

    //Este método garantiza que la información del maestro solo se cargue una vez
    fun ensureLoaded() {
        if (!state.value.loaded && !state.value.isLoading) {
            loadTeacherData()
        }
    }

    // Maneja las diferentes acciones (Intents) que el usuario puede realizar desde la UI
    fun onIntent(intent: TeacherContract.Intent, navigate: (AppDestination) -> Unit) {
        when (intent) {
            is TeacherContract.Intent.OpenCourseDetails -> navigate(CourseDetails(intent.courseId))
            TeacherContract.Intent.OpenProfile -> navigate(Profile)
            TeacherContract.Intent.OpenSecuritySettings -> navigate(SecuritySettings)
            TeacherContract.Intent.OpenAppearanceSettings -> navigate(AppearanceSettings)
            TeacherContract.Intent.Logout -> logout(navigate)
            TeacherContract.Intent.ToggleDrawer -> openDrawer()
            TeacherContract.Intent.CloseDrawer -> closeDrawer()
        }
    }

    /**
     * Carga la información principal del maestro:
     * - Perfil del usuario (nombre, email)
     * - Lista de cursos asignados
     */
    private fun loadTeacherData() {
        viewModelScope.launch {
            // Actualiza el estado a "cargando" y limpia errores previos
            state.value = state.value.copy(isLoading = true, error = "")

            // Obtiene el ID del usuario 
            val teacherId = currentUserUseCase()
            if (teacherId == null) {
                // Si no hay sesión se muestra error y detiene la carga
                state.value = state.value.copy(isLoading = false, error = "Sesión no encontrada")
                return@launch
            }

            //  Cargar perfil del maestro
            when (val resProfile = getTeacherProfileUseCase(teacherId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(user = resProfile.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando perfil: ${resProfile.throwable.message}")
                }
            }

            // Cargar cursos del maestro
            when (val resCourses = getTeacherCoursesUseCase(teacherId)) {
                is Result.Ok -> {
                    state.value = state.value.copy(courses = resCourses.value)
                }
                is Result.Err -> {
                    state.value = state.value.copy(error = "Error cargando cursos: ${resCourses.throwable.message}")
                }
            }

            // Finaliza la carga y marca el estado como “cargado correctamente”
            state.value = state.value.copy(isLoading = false, loaded = true)
        }
    }

    //Cierra sesión y redirige a la pantalla de login
    private fun logout(navigate: (AppDestination) -> Unit) {
        viewModelScope.launch {
            // Limpia el estado y llama al caso de uso de logout
            state.value = TeacherContract.State()
            logoutUseCase()
            navigate(Login)
        }
    }

    //Para abrir el menu
    private fun openDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Open))
    }

    //Para cerrar el menu
    private fun closeDrawer() {
        state.value = state.value.copy(isDrawerOpen = DrawerState(DrawerValue.Closed))
    }
}