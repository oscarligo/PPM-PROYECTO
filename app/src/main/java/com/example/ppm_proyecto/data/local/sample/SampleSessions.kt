package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.example.ppm_proyecto.domain.models.course.Enrollment
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone 

// Utilidades seguras para crear Timestamps desde strings
private fun tsDate(date: String): Timestamp {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }
    return try {
        val parsed = sdf.parse(date) ?: return Timestamp.now()
        Timestamp(parsed)
    } catch (_: ParseException) {
        Timestamp.now()
    }
}

private fun tsDateTime(date: String, time: String): Timestamp {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }
    return try {
        val parsed = sdf.parse("$date $time") ?: return Timestamp.now()
        Timestamp(parsed)
    } catch (_: ParseException) {
        Timestamp.now()
    }
}

// Usuarios de ejemplo
val sampleTeacherUsers = listOf(
    User(
        id = "teacher-1",
        name = "Ana Castillo",
        email = "ana.castillo@example.com",
        role = UserRole.Teacher,
        profileImageUrl = ""
    ),
    User (
        id = "teacher-2",
        name = "Jorge Martínez",
        email = "jorge.martinez@example.com",
        role = UserRole.Teacher,
        profileImageUrl = ""
    )
)

val sampleStudentUsers = listOf(
    User(
        id = "stu-1",
        name = "Luis Fernández",
        email = "luis.fernandez@example.com",
        role = UserRole.Student,
        profileImageUrl = "",
    ),

    User(
        id = "stu-2",
        name = "María Gómez",
        email = "maria.gomez@example.com",
        role = UserRole.Student,
        profileImageUrl = "",
    ),
    User(
        id = "stu-3",
        name = "Carlos Ruiz",
        email = "carlos.ruiz@example.com",
        role = UserRole.Student,
        profileImageUrl = "",
    ),
)

// Cursos de ejemplo
val sampleCourseMobile = Course(
    id = "C-MOV",
    name = "Programación Móvil",
    description = "Fundamentos de Android con Compose",
    teacherId = "teacher-1",
)

val sampleCourseAlgebra = Course(
    id = "C-ALG",
    name = "Álgebra",
    description = "Álgebra lineal básica",
    teacherId = "teacher-2",
)



// Lista de cursos consolidada
val sampleCourses: List<Course> = listOf(sampleCourseMobile, sampleCourseAlgebra)

// Inscripciones de ejemplo (studentId -> courseId)
val sampleEnrollments: List<Enrollment> = listOf(
    Enrollment(id = "enr-1", studentId = "stu-1", courseId = "C-MOV"),
    Enrollment(id = "enr-2", studentId = "stu-1", courseId = "C-ALG"),
    Enrollment(id = "enr-3", studentId = "stu-2", courseId = "C-MOV"),
    Enrollment(id = "enr-4", studentId = "stu-3", courseId = "C-ALG"),
)

// Sesiones de ejemplo para Programación Móvil (no existe courseId en el modelo)
val sampleMobileSessions: List<CourseSession> = listOf(
    CourseSession(
        id = "S-MOV-001",
        scheduledDate = tsDate("2024-10-01"),
        startTime = tsDateTime("2024-10-01", "09:00"),
        endTime = tsDateTime("2024-10-01", "11:00"),
        topic = "Introducción y setup",
    ),
    CourseSession(
        id = "S-MOV-002",
        scheduledDate = tsDate("2024-10-08"),
        startTime = tsDateTime("2024-10-08", "09:00"),
        endTime = tsDateTime("2024-10-08", "11:00"),
        topic = "Layouts y Componentes",
    ),
)

// Sesiones de ejemplo para Álgebra
val sampleAlgebraSessions: List<CourseSession> = listOf(
    CourseSession(
        id = "S-ALG-001",
        scheduledDate = tsDate("2024-10-02"),
        startTime = tsDateTime("2024-10-02", "10:00"),
        endTime = tsDateTime("2024-10-02", "12:00"),
        topic = "Vectores y matrices",
    ),
    CourseSession(
        id = "S-ALG-002",
        scheduledDate = tsDate("2024-10-09"),
        startTime = tsDateTime("2024-10-09", "10:00"),
        endTime = tsDateTime("2024-10-09", "12:00"),
        topic = "Determinantes",
    ),
)

// Asistencias de ejemplo por sesión (clave = sessionId)
val sampleSessionAttendancesBySessionId: Map<String, List<SessionAttendance>> = mapOf(
    "S-MOV-001" to listOf(
        SessionAttendance(id = "att-001", studentId = "stu-1", status = AttendanceStatus.Present.name),
        SessionAttendance(id = "att-002", studentId = "stu-2", status = AttendanceStatus.Late.name),
    ),
    "S-MOV-002" to listOf(
        SessionAttendance(id = "att-003", studentId = "stu-1", status = AttendanceStatus.Absent.name),
        SessionAttendance(id = "att-004", studentId = "stu-2", status = AttendanceStatus.Present.name),
    ),
    "S-ALG-001" to listOf(
        SessionAttendance(id = "att-005", studentId = "stu-1", status = AttendanceStatus.Present.name),
        SessionAttendance(id = "att-006", studentId = "stu-3", status = AttendanceStatus.Present.name),
    ),
    "S-ALG-002" to listOf(
        SessionAttendance(id = "att-007", studentId = "stu-1", status = AttendanceStatus.Late.name),
        SessionAttendance(id = "att-008", studentId = "stu-3", status = AttendanceStatus.Absent.name),
    ),
)