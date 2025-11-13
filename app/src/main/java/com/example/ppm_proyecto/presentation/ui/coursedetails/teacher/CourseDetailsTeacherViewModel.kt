package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus as DomainAttendanceStatus

// ViewModel que maneja toda la lógica de la pantalla
// Se conecta a Firebase y actualiza el estado de la UI
class CourseDetailsTeacherViewModel : ViewModel() {

    // Firebase
    private val db = FirebaseFirestore.getInstance()

    // Estado expuesto a la UI solo leee
    private val _state = MutableStateFlow(CourseDetailsTeacherState())
    val state: StateFlow<CourseDetailsTeacherState> = _state

    // Método principal para manejar las acciones de la UI (intents)
    fun handleIntent(intent: CourseDetailsTeacherIntent) {
        when (intent) {
            is CourseDetailsTeacherIntent.LoadCourse -> loadCourseData(intent.courseId)
            is CourseDetailsTeacherIntent.SelectDate -> updateDate(intent.date)
            CourseDetailsTeacherIntent.Refresh -> refreshData()
        }
    }

    // Carga los datos del curso (nombre, maestro y lista de estudiantes)
    private fun loadCourseData(courseId: String) {
        viewModelScope.launch {
            // Se guarda el id de curso en el estado para poder refrescar correctamente
            _state.value = _state.value.copy(isLoading = true, errorMessage = null, courseId = courseId)

            db.collection("courses").document(courseId).get()
                .addOnSuccessListener { courseDoc ->
                    val courseName = courseDoc.getString("name") ?: "Curso sin nombre"
                    val teacherName = courseDoc.getString("teacherName") ?: "Desconocido"

                    // Traer lista de estudiantes dentro del curso
                    //Trae con su respectiva asistencia
                    db.collection("courses").document(courseId)
                        .collection("students")
                        .get()
                        .addOnSuccessListener { studentsSnapshot ->
                            // Mapear attendance (string) a la enum del dominio
                            val students = studentsSnapshot.map { doc ->
                                val raw = doc.getString("attendance")
                                val status = mapToDomainAttendance(raw)
                                StudentAttendance(
                                    name = doc.getString("name") ?: "Sin nombre",
                                    status = status
                                )
                            }

                                    _state.value = _state.value.copy(
                                        isLoading = false,
                                        courseName = courseName,
                                        teacherName = teacherName,
                                        students = students
                                    )
                        }
                        .addOnFailureListener {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = "Error al cargar los estudiantes"
                            )
                        }
                }
                .addOnFailureListener {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar el curso"
                    )
                }
        }
    }

    // Actualiza la fecha seleccionada por el maestro
    private fun updateDate(date: LocalDate) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    // Recarga los datos (por ejemplo, tras editar asistencia)
    //TODAVIA NO IMPLEMENTADA
    private fun refreshData() {
        // Usar el courseId guardado (más fiable que el nombre)
        val currentCourseId = _state.value.courseId
        if (!currentCourseId.isNullOrBlank()) {
            // relanzar la carga real
            loadCourseData(currentCourseId)
        } else {
            // nada que refrescar
        }
    }

    // Mapeo robusto desde cadenas (posible español/inglés, mayúsc/minúsc) a la enum de dominio
    //Se hace para evitar problemas de lenguaje nada más
    private fun mapToDomainAttendance(raw: String?): DomainAttendanceStatus {
        if (raw.isNullOrBlank()) return DomainAttendanceStatus.Present
        val normalized = raw.trim().lowercase(Locale.ROOT)
        return when {
            // inglés
            normalized.contains("present") -> DomainAttendanceStatus.Present
            normalized.contains("late") -> DomainAttendanceStatus.Late
            normalized.contains("absent") -> DomainAttendanceStatus.Absent
            // español (por si la base tiene valores en español)
            normalized.contains("presente") -> DomainAttendanceStatus.Present
            normalized.contains("tarde") -> DomainAttendanceStatus.Late
            normalized.contains("ausente") -> DomainAttendanceStatus.Absent
            // como fallback, intentar valueOf con capitalized
            else -> try {
                val candidate = normalized.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                DomainAttendanceStatus.valueOf(candidate)
            } catch (_: Exception) {
                DomainAttendanceStatus.Present
            }
        }
    }
}
