package com.example.ppm_proyecto.presentation.ui.coursedetails.teacher


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.presentation.ui.coursedetails.teacher.StudentAttendance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsTeacherScreen(
    //Aqui se usa el view Model
    viewModel: CourseDetailsTeacherViewModel = viewModel(),
    onOpenDrawer: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onEditClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedDate by remember { mutableStateOf(state.selectedDate) }
    val students = state.students

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = state.teacherName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Título del curso
                Text(
                    text = state.courseName.ifBlank { "Curso" },
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de fecha
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Fecha: ${state.selectedDate}",
                        modifier = Modifier.weight(1f)
                    )

                    Button(onClick = { onDateSelected(state.selectedDate) }) {
                        Text("Seleccionar fecha")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Subtitulo
                Text(
                    text = "Estudiantes",
                    
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Listado de estudiantes
                //LazyColumn porque sera una cantidad finita
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(students) { student ->
                        StudentAttendanceCard(student)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onEditClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Editar")
                }
            }
        }
    )
}

// Función de como se mostrará a un estudiante
@Composable
fun StudentAttendanceCard(student: StudentAttendance) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(student.name)
            AttendanceIcon(status = student.status)
        }
    }
}

//Para mostrar los iconos
@Composable
fun AttendanceIcon(status: AttendanceStatus) {
    val emoji = when (status) {
        AttendanceStatus.Present -> "✅"
        AttendanceStatus.Late -> "⚠️"
        AttendanceStatus.Absent -> "❌"
    }
    Text(emoji, style = MaterialTheme.typography.titleMedium)
}

// Preview con datos simulados
@Preview(showBackground = true)
@Composable
fun CourseDetailScreenPreview() {
    // Preview usa callbacks vacíos; el ViewModel por defecto no tendrá datos en preview
    CourseDetailsTeacherScreen(
        onOpenDrawer = {},
        onDateSelected = {},
        onEditClicked = {}
    )

}
