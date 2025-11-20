package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.DayOfWeek
import javax.inject.Inject
import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.usecase.teacher.GetCourseStudentsUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetSessionAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.CreateSessionUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetCourseSessionsForTeacherUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.MarkStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.TimeZone
import java.time.format.DateTimeFormatter

/**
 * ViewModel que maneja toda la lógica de la pantalla de detalles del curso para profesores.
 * Usa inyección de dependencias de Hilt y los use cases del dominio.
 */
@HiltViewModel
class CourseDetailsTeacherViewModel @Inject constructor(
    private val getCourseStudentsUseCase: GetCourseStudentsUseCase,
    private val getSessionAttendanceUseCase: GetSessionAttendanceUseCase,
    @Suppress("unused") private val getUserUseCase: GetUserUseCase, // Mantener inyección para posible uso futuro
    private val createSessionUseCase: CreateSessionUseCase,
    private val getCourseSessionsForTeacherUseCase: GetCourseSessionsForTeacherUseCase,
    private val markStudentAttendanceUseCase: MarkStudentAttendanceUseCase
) : ViewModel() {

    // Estado expuesto a la UI (solo lectura)
    private val _state = MutableStateFlow(CourseDetailsTeacherState())
    val state: StateFlow<CourseDetailsTeacherState> = _state

    // Método principal para manejar las acciones de la UI (intents)
    fun handleIntent(intent: CourseDetailsTeacherIntent) {
        when (intent) {
            is CourseDetailsTeacherIntent.LoadCourse -> loadCourseData(intent.courseId)
            is CourseDetailsTeacherIntent.SelectDate -> updateDate(intent.date)
            CourseDetailsTeacherIntent.Refresh -> refreshData()
            // --- Creación de sesiones ---
            CourseDetailsTeacherIntent.OpenCreateSessionDialog -> _state.value = _state.value.copy(
                showCreateSessionDialog = true,
                creationError = null,
                creationSuccess = null,
                showSingleDatePicker = false,
                showRangeStartPicker = false,
                showRangeEndPicker = false
            )
            CourseDetailsTeacherIntent.CloseCreateSessionDialog -> _state.value = _state.value.copy(
                showCreateSessionDialog = false,
                showSingleDatePicker = false,
                showRangeStartPicker = false,
                showRangeEndPicker = false
            )
            is CourseDetailsTeacherIntent.SetCreationMode -> _state.value = _state.value.copy(creationMode = intent.mode)
            is CourseDetailsTeacherIntent.SetSingleSessionDate -> _state.value = _state.value.copy(singleSessionDate = intent.date)
            is CourseDetailsTeacherIntent.SetRangeStartDate -> _state.value = _state.value.copy(rangeStartDate = intent.date)
            is CourseDetailsTeacherIntent.SetRangeEndDate -> _state.value = _state.value.copy(rangeEndDate = intent.date)
            is CourseDetailsTeacherIntent.ToggleWeekday -> toggleWeekday(intent.day)
            is CourseDetailsTeacherIntent.SetStartTime -> _state.value = _state.value.copy(startTimeText = intent.value)
            is CourseDetailsTeacherIntent.SetEndTime -> _state.value = _state.value.copy(endTimeText = intent.value)
            CourseDetailsTeacherIntent.SubmitCreateSessions -> submitCreateSessions()
            CourseDetailsTeacherIntent.ClearCreationFeedback -> _state.value = _state.value.copy(creationError = null, creationSuccess = null)
            is CourseDetailsTeacherIntent.SelectSession -> selectSession(intent.sessionId)
            is CourseDetailsTeacherIntent.ChangeAttendance -> changeAttendance(intent.studentId, intent.status)
            CourseDetailsTeacherIntent.ShowSingleDatePicker -> _state.value = _state.value.copy(showSingleDatePicker = true)
            CourseDetailsTeacherIntent.HideSingleDatePicker -> _state.value = _state.value.copy(showSingleDatePicker = false)
            CourseDetailsTeacherIntent.ShowRangeStartPicker -> _state.value = _state.value.copy(showRangeStartPicker = true)
            CourseDetailsTeacherIntent.HideRangeStartPicker -> _state.value = _state.value.copy(showRangeStartPicker = false)
            CourseDetailsTeacherIntent.ShowRangeEndPicker -> _state.value = _state.value.copy(showRangeEndPicker = true)
            CourseDetailsTeacherIntent.HideRangeEndPicker -> _state.value = _state.value.copy(showRangeEndPicker = false)
            CourseDetailsTeacherIntent.ShowSelectedDatePicker -> _state.value = _state.value.copy(showSelectedDatePicker = true)
            CourseDetailsTeacherIntent.HideSelectedDatePicker -> _state.value = _state.value.copy(showSelectedDatePicker = false)
            CourseDetailsTeacherIntent.ToggleShowAllSessions -> _state.value = _state.value.copy(showAllSessions = !_state.value.showAllSessions)
        }
    }

    private fun toggleWeekday(day: DayOfWeek) {
        val current = _state.value.selectedWeekdays.toMutableSet()
        if (!current.add(day)) current.remove(day)
        _state.value = _state.value.copy(selectedWeekdays = current)
    }

    /**
     * Carga los datos del curso y los estudiantes asignados
     */
    private fun loadCourseData(courseId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null, courseId = courseId)
            try {
                val studentsResult = getCourseStudentsUseCase(courseId)
                val sessionsResult = getCourseSessionsForTeacherUseCase(courseId)
                val users = when (studentsResult) { is Result.Ok -> studentsResult.value; is Result.Err -> emptyList() }
                val sessions = when (sessionsResult) { is Result.Ok -> sessionsResult.value; is Result.Err -> emptyList() }

                // Elegir sesión correspondiente a la fecha seleccionada
                val selectedDate = _state.value.selectedDate
                val matchedSession = sessions.firstOrNull { cs ->
                    cs.scheduledDate?.toDate()?.let { d ->
                        val ld = java.time.Instant.ofEpochMilli(d.time).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                        ld == selectedDate
                    } ?: false
                }

                val sessionAttendance: List<SessionAttendance> = if (matchedSession != null) {
                    when (val attRes = getSessionAttendanceUseCase(courseId, matchedSession.id)) {
                        is Result.Ok -> {
                            val existing = attRes.value
                            // Construir mapa para buscar rápidos
                            val existingByStudent = existing.associateBy { it.studentId }
                            users.map { u ->
                                existingByStudent[u.id] ?: SessionAttendance(
                                    id = matchedSession.id + "-" + u.id,
                                    studentId = u.id,
                                    courseId = courseId,
                                    sessionId = matchedSession.id,
                                    status = AttendanceStatus.Absent,
                                    entryTime = Timestamp.now()
                                )
                            }
                        }
                        is Result.Err -> users.map { u ->
                            SessionAttendance(
                                id = "temp-${u.id}",
                                studentId = u.id,
                                courseId = courseId,
                                sessionId = matchedSession.id,
                                status = AttendanceStatus.Absent,
                                entryTime = Timestamp.now()
                            )
                        }
                    }
                } else {
                    // No hay sesión en esta fecha, lista vacía
                    emptyList()
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    students = sessionAttendance,
                    sessions = sessions,
                    selectedSessionId = matchedSession?.id,
                    studentNames = users.associate { it.id to it.name },
                    courseName = "Curso"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, errorMessage = "Error inesperado: ${e.message}")
            }
        }
    }

    /**
     * Actualiza la fecha seleccionada por el profesor
     */
    private fun updateDate(date: LocalDate) {
        _state.value = _state.value.copy(selectedDate = date)
        val courseId = _state.value.courseId ?: return
        // Recargar datos para actualizar asistencia de la nueva fecha
        loadCourseData(courseId)
    }

    /**
     * Recarga los datos del curso (útil tras editar asistencia)
     */
    private fun refreshData() {
        val currentCourseId = _state.value.courseId
        if (!currentCourseId.isNullOrBlank()) {
            loadCourseData(currentCourseId)
        }
    }

    // --- Creación de sesiones ---
    private fun submitCreateSessions() {
        val courseId = _state.value.courseId ?: run {
            _state.value = _state.value.copy(creationError = "No hay curso cargado")
            return
        }
        val startTime = parseTime(_state.value.startTimeText)
        val endTime = parseTime(_state.value.endTimeText)
        if (startTime == null || endTime == null) {
            _state.value = _state.value.copy(creationError = "Formato de hora inválido. Use HH:mm")
            return
        }
        if (!endTime.isAfter(startTime)) {
            _state.value = _state.value.copy(creationError = "La hora fin debe ser después de la hora inicio")
            return
        }
        if (_state.value.creationMode == SessionCreationMode.Recurring && _state.value.rangeEndDate.isBefore(_state.value.rangeStartDate)) {
            _state.value = _state.value.copy(creationError = "El fin del rango debe ser posterior al inicio")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isCreatingSessions = true, creationError = null, creationSuccess = null)
            val sessionsToCreate = when (_state.value.creationMode) {
                SessionCreationMode.Single -> listOf(_state.value.singleSessionDate)
                SessionCreationMode.Recurring -> buildRecurringDates(_state.value.rangeStartDate, _state.value.rangeEndDate, _state.value.selectedWeekdays)
            }
            if (sessionsToCreate.isEmpty()) {
                _state.value = _state.value.copy(isCreatingSessions = false, creationError = "No hay fechas para crear sesiones")
                return@launch
            }

            // Obtener el número actual de sesiones para continuar la numeración
            val currentSessionCount = _state.value.sessions.size

            var successCount = 0
            var failCount = 0
            for ((index, date) in sessionsToCreate.withIndex()) {
                val sessionNumber = currentSessionCount + index + 1
                val dayOfWeek = getDayOfWeekInSpanish(date.dayOfWeek)
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val topic = "Sesión $sessionNumber - $dayOfWeek $formattedDate"

                val session = CourseSession(
                    id = "", // remote asignará si está vacío
                    scheduledDate = toTimestamp(date, startTime),
                    startTime = toTimestamp(date, startTime),
                    endTime = toTimestamp(date, endTime),
                    topic = topic
                )
                when (val res = createSessionUseCase(courseId, session)) {
                    is Result.Ok -> if (res.value) successCount++ else failCount++
                    is Result.Err -> failCount++
                }
            }
            _state.value = _state.value.copy(
                isCreatingSessions = false,
                creationSuccess = "Sesiones creadas: $successCount / ${sessionsToCreate.size}",
                creationError = if (failCount > 0) "Fallaron $failCount sesiones" else null
            )
            refreshData()
        }
    }

    private fun buildRecurringDates(start: LocalDate, end: LocalDate, weekdays: Set<DayOfWeek>): List<LocalDate> {
        if (end.isBefore(start)) return emptyList()
        if (weekdays.isEmpty()) return emptyList()
        val dates = mutableListOf<LocalDate>()
        var current = start
        while (!current.isAfter(end)) {
            if (weekdays.contains(current.dayOfWeek)) dates += current
            current = current.plusDays(1)
        }
        return dates
    }

    private fun parseTime(raw: String): LocalTime? = try {
        LocalTime.parse(raw.trim())
    } catch (_: Exception) { null }

    private fun toTimestamp(date: LocalDate, time: LocalTime): Timestamp {
        val cal = Calendar.getInstance() // Usa zona horaria local por defecto
        cal.set(Calendar.YEAR, date.year)
        cal.set(Calendar.MONTH, date.monthValue - 1)
        cal.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
        cal.set(Calendar.HOUR_OF_DAY, time.hour)
        cal.set(Calendar.MINUTE, time.minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return Timestamp(cal.time)
    }

    private fun selectSession(sessionId: String) {
        val courseId = _state.value.courseId ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, selectedSessionId = sessionId, attendanceError = null)
            when (val attRes = getSessionAttendanceUseCase(courseId, sessionId)) {
                is Result.Ok -> {
                    val existing = attRes.value.associateBy { it.studentId }
                    val usersResult = getCourseStudentsUseCase(courseId)
                    val users = if (usersResult is Result.Ok) usersResult.value else emptyList()
                    val merged = users.map { u ->
                        existing[u.id] ?: SessionAttendance(
                            id = "$sessionId-${u.id}",
                            studentId = u.id,
                            courseId = courseId,
                            sessionId = sessionId,
                            status = AttendanceStatus.Absent,
                            entryTime = Timestamp.now()
                        )
                    }
                    _state.value = _state.value.copy(isLoading = false, students = merged, attendanceMessage = null)
                }
                is Result.Err -> {
                    _state.value = _state.value.copy(isLoading = false, attendanceError = attRes.throwable.message ?: "Error cargando asistencia")
                }
            }
        }
    }

    private fun changeAttendance(studentId: String, status: AttendanceStatus) {
        val courseId = _state.value.courseId ?: return
        val sessionId = _state.value.selectedSessionId ?: return
        val updatedList = _state.value.students.map { sa -> if (sa.studentId == studentId) sa.copy(status = status) else sa }
        _state.value = _state.value.copy(students = updatedList, isUpdatingAttendance = true, attendanceError = null)
        viewModelScope.launch {
            when (val res = markStudentAttendanceUseCase(courseId, sessionId, studentId, status.name)) {
                is Result.Ok -> _state.value = _state.value.copy(isUpdatingAttendance = false, attendanceMessage = "Guardado")
                is Result.Err -> {
                    val reverted = _state.value.students.map { sa -> if (sa.studentId == studentId) sa.copy(status = AttendanceStatus.Absent) else sa }
                    _state.value = _state.value.copy(students = reverted, isUpdatingAttendance = false, attendanceError = res.throwable.message ?: "Error al guardar")
                }
            }
        }
    }

    private fun getDayOfWeekInSpanish(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Lunes"
            DayOfWeek.TUESDAY -> "Martes"
            DayOfWeek.WEDNESDAY -> "Miércoles"
            DayOfWeek.THURSDAY -> "Jueves"
            DayOfWeek.FRIDAY -> "Viernes"
            DayOfWeek.SATURDAY -> "Sábado"
            DayOfWeek.SUNDAY -> "Domingo"
        }
    }
}
