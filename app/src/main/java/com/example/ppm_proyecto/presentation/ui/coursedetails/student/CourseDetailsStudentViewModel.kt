package com.example.ppm_proyecto.presentation.ui.coursedetails.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus

/**
 * ViewModel para la pantalla de detalles del curso (estudiante)
 * Utiliza los UseCases de la capa de dominio para obtener datos de Firebase
 */
@HiltViewModel
class CourseDetailsStudentViewModel @Inject constructor(
    private val getStudentCoursesUseCase: GetStudentCoursesUseCase,
    private val getStudentAttendanceUseCase: GetStudentAttendanceUseCase,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _state = MutableStateFlow(CourseDetailsStudentState())
    val state: StateFlow<CourseDetailsStudentState> = _state.asStateFlow()

    private var currentCourseId: String? = null
    private var currentStudentId: String? = null

    /**
     * Procesa las intenciones del usuario
     */
    fun handleIntent(intent: CourseDetailsStudentIntent) {
        when (intent) {
            is CourseDetailsStudentIntent.LoadCourseDetails -> {
                loadCourseDetails(intent.courseId, intent.studentId)
            }
            is CourseDetailsStudentIntent.RetryLoading -> {
                currentCourseId?.let { courseId ->
                    currentStudentId?.let { studentId ->
                        loadCourseDetails(courseId, studentId)
                    }
                }
            }
            is CourseDetailsStudentIntent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    /**
     * Carga los detalles del curso y la asistencia del estudiante
     */
    private fun loadCourseDetails(courseId: String, studentId: String) {
        currentCourseId = courseId
        currentStudentId = studentId

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Cargar información del usuario
                val userSnapshot = db.collection("users")
                    .document(studentId)
                    .get()
                    .await()

                val userName = userSnapshot.getString("name") ?: "Usuario"
                val userProfileUrl = userSnapshot.getString("profileImageUrl") ?: ""

                // Cargar cursos del estudiante usando el UseCase
                val coursesResult = getStudentCoursesUseCase.invoke(studentId)

                when (coursesResult) {
                    is Result.Ok -> {
                        val coursesList = coursesResult.value

                        // Encontrar el curso específico
                        var course: com.example.ppm_proyecto.domain.models.course.Course? = null
                        val coursesIterator = coursesList.iterator()
                        while (coursesIterator.hasNext()) {
                            val c = coursesIterator.next()
                            if (c.id == courseId) {
                                course = c
                                break
                            }
                        }

                        if (course == null) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No se pudo encontrar el curso"
                                )
                            }
                            return@launch
                        }

                        // Cargar registros de asistencia usando el UseCase
                        val attendanceResult = getStudentAttendanceUseCase.invoke(courseId, studentId)

                        when (attendanceResult) {
                            is Result.Ok -> {
                                val attendanceList = attendanceResult.value

                                // Calcular estadísticas de asistencia manualmente
                                var presentCount = 0
                                var absentCount = 0
                                var lateCount = 0
                                var totalDays = 0

                                val attendanceIterator = attendanceList.iterator()
                                while (attendanceIterator.hasNext()) {
                                    val record = attendanceIterator.next()
                                    totalDays++

                                    when (record.status) {
                                        AttendanceStatus.Present -> presentCount++
                                        AttendanceStatus.Absent -> absentCount++
                                        AttendanceStatus.Late -> lateCount++
                                    }
                                }

                                val attendancePercent = if (totalDays > 0) {
                                    ((presentCount.toFloat() / totalDays) * 100).toInt()
                                } else 0

                                // Actualizar el estado con toda la información cargada
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        course = course,
                                        attendanceRecords = attendanceList,
                                        presentCount = presentCount,
                                        absentCount = absentCount,
                                        lateCount = lateCount,
                                        attendancePercent = attendancePercent,
                                        userName = userName,
                                        userProfileUrl = userProfileUrl,
                                        error = null
                                    )
                                }
                            }
                            is Result.Err -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = "Error al cargar asistencia"
                                    )
                                }
                            }
                        }
                    }
                    is Result.Err -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al cargar cursos"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los datos: ${e.message}"
                    )
                }
            }
        }
    }
}