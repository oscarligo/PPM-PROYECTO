package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.presentation.components.LoadingOverlay
import com.example.ppm_proyecto.presentation.components.ReadOnlyFieldWithCopy
import com.example.ppm_proyecto.presentation.components.SecondaryTopBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsTeacherScreen(
    courseId: String,
    viewModel: CourseDetailsTeacherViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onEditClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(courseId) { viewModel.handleIntent(CourseDetailsTeacherIntent.LoadCourse(courseId)) }

    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = state.courseName.ifBlank { "Detalles del Curso" },
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.OpenCreateSessionDialog) }) { Text("+") }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                state.errorMessage?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Error", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.height(8.dp))
                            Text(error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.Refresh) }) { Text("Reintentar") }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Text(state.courseName.ifBlank { "Curso" }, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                ReadOnlyFieldWithCopy(label = "ID del curso", value = state.courseId, icon = Icons.Default.Info)
                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Fecha: ${state.selectedDate}",
                        modifier = Modifier.weight(1f)
                    )

                    Button(onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.ShowSelectedDatePicker) }) {
                        Text("Cambiar fecha")
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Botón para alternar vista de sesiones
                Button(
                    onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.ToggleShowAllSessions) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.showAllSessions) "Filtrar por fecha" else "Ver todas las sesiones")
                }
                Spacer(Modifier.height(16.dp))

                // Lista de sesiones - filtrada por fecha o todas
                val sessionsToShow = remember(state.sessions, state.selectedDate, state.showAllSessions) {
                    if (state.showAllSessions) {
                        state.sessions.sortedByDescending { it.scheduledDate }
                    } else {
                        state.sessions.filter { session ->
                            session.scheduledDate?.toDate()?.let { d ->
                                val ld = Instant.ofEpochMilli(d.time).atZone(ZoneId.systemDefault()).toLocalDate()
                                ld == state.selectedDate
                            } ?: false
                        }
                    }
                }

                if (sessionsToShow.isNotEmpty()) {
                    Text(
                        if (state.showAllSessions) "Todas las sesiones (${sessionsToShow.size})"
                        else "Sesiones del ${state.selectedDate}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    if (state.showAllSessions) {
                        // Vista vertical para todas las sesiones
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sessionsToShow) { session ->
                                val selected = session.id == state.selectedSessionId
                                val sessionDate = session.scheduledDate?.toDate()?.let { d ->
                                    Instant.ofEpochMilli(d.time).atZone(ZoneId.systemDefault()).toLocalDate()
                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.handleIntent(CourseDetailsTeacherIntent.SelectSession(session.id)) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            session.topic.ifBlank { "Sesión ${session.id}" },
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        sessionDate?.let {
                                            Text(
                                                "Fecha: $it",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Vista horizontal para sesiones de la fecha
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(sessionsToShow) { session ->
                                val selected = session.id == state.selectedSessionId
                                AssistChip(
                                    onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.SelectSession(session.id)) },
                                    label = { Text(session.topic.ifBlank { session.id }) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                } else if (!state.showAllSessions) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No hay sesiones programadas para ${state.selectedDate}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Lista de estudiantes - solo se muestra si hay una sesión seleccionada
                if (state.selectedSessionId != null) {
                    Text("Estudiantes (${state.students.size})", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(10.dp))

                    if (state.students.isEmpty() && !state.isLoading) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No hay estudiantes registrados en esta sesión", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    } else if (state.students.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false)
                                .heightIn(max = 400.dp)
                        ) {
                            items(state.students) { attendance ->
                                AttendanceRow(
                                    att = attendance,
                                    name = state.studentNames[attendance.studentId],
                                    selectedSessionId = state.selectedSessionId,
                                    onChangeStatus = { newStatus -> viewModel.handleIntent(CourseDetailsTeacherIntent.ChangeAttendance(attendance.studentId, newStatus)) }
                                )
                            }
                        }
                    }
                } else {
                    // Mensaje cuando no hay sesión seleccionada
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Selecciona una sesión para ver la lista de estudiantes",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onEditClicked,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !state.isLoading
                ) { Text("Editar") }

                Button(
                    onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.OpenCreateSessionDialog) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Crear sesiones") }
            }
            if (state.isLoading) LoadingOverlay()
        }
    }

    if (state.showCreateSessionDialog) {
        CreateSessionsDialog(state = state, onIntent = { viewModel.handleIntent(it) })
    }

    // Snackbar para mensajes de asistencia
    state.attendanceMessage?.let { msg ->
        SnackbarHost(hostState = remember { SnackbarHostState() })
        LaunchedEffect(msg) { /* Podrías implementar Snackbar real con Scaffold */ }
    }

    if (state.showSelectedDatePicker) {
        val millis = remember(state.selectedDate) { state.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = millis)
        DatePickerDialog(
            onDismissRequest = { viewModel.handleIntent(CourseDetailsTeacherIntent.HideSelectedDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { selMillis ->
                        val ld = Instant.ofEpochMilli(selMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.handleIntent(CourseDetailsTeacherIntent.SelectDate(ld))
                        onDateSelected(ld) // mantener callback externo si existe
                    }
                    viewModel.handleIntent(CourseDetailsTeacherIntent.HideSelectedDatePicker)
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { viewModel.handleIntent(CourseDetailsTeacherIntent.HideSelectedDatePicker) }) { Text("Cancelar") } }
        ) { DatePicker(state = pickerState) }
    }
}

@Composable
fun AttendanceRow(
    att: SessionAttendance,
    name: String?,
    selectedSessionId: String?,
    onChangeStatus: (AttendanceStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(name ?: att.studentId, style = MaterialTheme.typography.bodyLarge)
                    selectedSessionId?.let { sid -> Text("Sesión: $sid", style = MaterialTheme.typography.labelSmall) }
                }
                AttendanceIcon(status = att.status)
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AttendanceStatus.entries.forEach { s ->
                    FilterChip(
                        selected = att.status == s,
                        onClick = { onChangeStatus(s) },
                        label = { Text(when(s){AttendanceStatus.Present->"Presente"; AttendanceStatus.Late->"Tarde"; AttendanceStatus.Absent->"Ausente"}) }
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceIcon(status: AttendanceStatus) {
    val emoji = when (status) {
        AttendanceStatus.Present ->  "✅"
        AttendanceStatus.Late -> "⚠️"
        AttendanceStatus.Absent -> "❌"
    }
    Text(emoji, style = MaterialTheme.typography.titleMedium)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateSessionsDialog(state: CourseDetailsTeacherState, onIntent: (CourseDetailsTeacherIntent) -> Unit) {
    val dateFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE }

    // AlertDialog principal
    AlertDialog(
        onDismissRequest = { onIntent(CourseDetailsTeacherIntent.CloseCreateSessionDialog) },
        confirmButton = {
            TextButton(
                onClick = { onIntent(CourseDetailsTeacherIntent.SubmitCreateSessions) },
                enabled = !state.isCreatingSessions
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = { onIntent(CourseDetailsTeacherIntent.CloseCreateSessionDialog) }) {
                Text("Cancelar")
            }
        },
        title = { Text("Crear sesiones") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = state.creationMode == SessionCreationMode.Single,
                        onClick = { onIntent(CourseDetailsTeacherIntent.SetCreationMode(SessionCreationMode.Single)) },
                        label = { Text("Única") }
                    )
                    Spacer(Modifier.width(8.dp))
                    FilterChip(
                        selected = state.creationMode == SessionCreationMode.Recurring,
                        onClick = { onIntent(CourseDetailsTeacherIntent.SetCreationMode(SessionCreationMode.Recurring)) },
                        label = { Text("Recurrente") }
                    )
                }
                if (state.creationMode == SessionCreationMode.Single) {
                    SelectableDateField(
                        label = "Fecha única",
                        value = dateFormatter.format(state.singleSessionDate),
                        onClick = { onIntent(CourseDetailsTeacherIntent.ShowSingleDatePicker) }
                    )
                } else {
                    SelectableDateField(
                        label = "Inicio rango",
                        value = dateFormatter.format(state.rangeStartDate),
                        onClick = { onIntent(CourseDetailsTeacherIntent.ShowRangeStartPicker) }
                    )
                    SelectableDateField(
                        label = "Fin rango",
                        value = dateFormatter.format(state.rangeEndDate),
                        onClick = { onIntent(CourseDetailsTeacherIntent.ShowRangeEndPicker) }
                    )
                    WeekdaySelector(selected = state.selectedWeekdays, onToggle = { onIntent(CourseDetailsTeacherIntent.ToggleWeekday(it)) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.startTimeText,
                        onValueChange = { onIntent(CourseDetailsTeacherIntent.SetStartTime(it)) },
                        label = { Text("Hora inicio (HH:mm)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state.endTimeText,
                        onValueChange = { onIntent(CourseDetailsTeacherIntent.SetEndTime(it)) },
                        label = { Text("Hora fin (HH:mm)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                state.creationError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                state.creationSuccess?.let { Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall) }
                if (state.isCreatingSessions) { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            }
        }
    )

    // Date pickers separados - se renderizan encima del AlertDialog cuando están activos
    if (state.showSingleDatePicker) {
        SingleDatePickerDialog(
            currentDate = state.singleSessionDate,
            onDateSelected = { onIntent(CourseDetailsTeacherIntent.SetSingleSessionDate(it)) },
            onDismiss = { onIntent(CourseDetailsTeacherIntent.HideSingleDatePicker) }
        )
    }

    if (state.showRangeStartPicker) {
        RangeStartDatePickerDialog(
            currentDate = state.rangeStartDate,
            rangeEndDate = state.rangeEndDate,
            onDateSelected = { newStart ->
                onIntent(CourseDetailsTeacherIntent.SetRangeStartDate(newStart))
                if (newStart.isAfter(state.rangeEndDate)) {
                    onIntent(CourseDetailsTeacherIntent.SetRangeEndDate(newStart))
                }
            },
            onDismiss = { onIntent(CourseDetailsTeacherIntent.HideRangeStartPicker) }
        )
    }

    if (state.showRangeEndPicker) {
        RangeEndDatePickerDialog(
            currentDate = state.rangeEndDate,
            rangeStartDate = state.rangeStartDate,
            onDateSelected = { newEnd ->
                if (!newEnd.isBefore(state.rangeStartDate)) {
                    onIntent(CourseDetailsTeacherIntent.SetRangeEndDate(newEnd))
                } else {
                    onIntent(CourseDetailsTeacherIntent.SetRangeEndDate(state.rangeStartDate))
                }
            },
            onDismiss = { onIntent(CourseDetailsTeacherIntent.HideRangeEndPicker) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleDatePickerDialog(
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = remember(currentDate) {
        currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { millis ->
                    val ld = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(ld)
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = pickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RangeStartDatePickerDialog(
    currentDate: LocalDate,
    rangeEndDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = remember(currentDate) {
        currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { millis ->
                    val ld = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(ld)
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = pickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RangeEndDatePickerDialog(
    currentDate: LocalDate,
    rangeStartDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = remember(currentDate) {
        currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { millis ->
                    val ld = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(ld)
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = pickerState)
    }
}

@Composable
fun WeekdaySelector(selected: Set<DayOfWeek>, onToggle: (DayOfWeek) -> Unit) {
    val days = DayOfWeek.entries
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        for (d in days) {
            val sel = selected.contains(d)
            AssistChip(
                onClick = { onToggle(d) },
                label = { Text(d.name.substring(0,3)) },
                colors = AssistChipDefaults.assistChipColors(containerColor = if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(Modifier.width(4.dp))
        }
    }
}

@Composable
private fun SelectableDateField(label: String, value: String, onClick: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
