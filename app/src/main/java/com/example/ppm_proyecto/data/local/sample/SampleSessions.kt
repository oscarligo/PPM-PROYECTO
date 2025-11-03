package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.models.course.AttendanceStatus
import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.course.SessionAttendance
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.models.user.UserRole
import com.example.ppm_proyecto.domain.models.course.Enrollment

val sampleTeacherUsers = listOf(
    User(
    id = "teacher-1",
    name = "Ana Castillo",
    email = "123",
    password = "password123",
    role = UserRole.Teacher,
    profileImageUrl = ""
    ),

    User (
    id = "teacher-2",
    name = "Jorge Martínez",
    email = "456",
    password = "password456",
    role = UserRole.Teacher,
    profileImageUrl = ""
    )
)

val sampleStudentUsers = listOf(
    User(
        id = "stu-1",
        name = "Luis Fernández",
        email = "123",
        password = "password123",
        role = UserRole.Student,
        profileImageUrl = "",
    ),

    User(
        id = "stu-2",
        name = "María Gómez",
        email = "456",
        password = "password456",
        role = UserRole.Student,
        profileImageUrl = "",
    ),
    User(
        id = "stu-3",
        name = "Carlos Ruiz",
        email = "789",
        password = "password789",
        role = UserRole.Student,
        profileImageUrl = "",
    ),

)

val sampleEnrolments = listOf(
    Enrollment(
        studentId = "stu-1",
        courseId = "C-MOV"
    ),
    Enrollment(
        studentId = "stu-1",
        courseId = "C-ALG"
    ),
    Enrollment(
        studentId = "stu-2",
        courseId = "C-MOV"
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


// Sesiones de ejemplo para Programación Móvil
val sampleMobileSessions: List<CourseSession> = listOf(
    CourseSession(
        id = "S-MOV-001",
        courseId = "C-MOV",
        date = "2024-10-01",
        startTime = "09:00",
        endTime = "11:00",
        topic = "Introducción y setup",
        attendance = listOf(
            SessionAttendance("stu-1", AttendanceStatus.Presente),
            SessionAttendance("stu-2", AttendanceStatus.Tarde),
            SessionAttendance("stu-3", AttendanceStatus.Ausente),
        )
    ),
    CourseSession(
        id = "S-MOV-002",
        courseId = "C-MOV",
        date = "2024-10-08",
        startTime =  "09:00",
        endTime =  "11:00",
        topic = "Layouts y Componentes",
        attendance = listOf(
            SessionAttendance("stu-1", AttendanceStatus.Presente),
            SessionAttendance("stu-2", AttendanceStatus.Presente),
            SessionAttendance("stu-3", AttendanceStatus.Tarde),
        )
    ),
)

// Sesiones de ejemplo para Álgebra
val sampleAlgebraSessions: List<CourseSession> = listOf(
    CourseSession(
        id = "S-ALG-001",
        courseId = "C-ALG",
        date = "2024-10-02",
        topic = "Vectores y matrices",
        startTime =  "10:00",
        endTime =  "12:00",
        attendance = listOf(
            SessionAttendance("stu-1", AttendanceStatus.Ausente),
            SessionAttendance("stu-2", AttendanceStatus.Presente),
            SessionAttendance("stu-3", AttendanceStatus.Presente),
        )
    ),
    CourseSession(
        id = "S-ALG-002",
        courseId = "C-ALG",
        date = "2024-10-09",
        topic = "Determinantes",
        startTime =  "10:00",
        endTime =  "12:00",
        attendance = listOf(
            SessionAttendance("stu-1", AttendanceStatus.Tarde),
            SessionAttendance("stu-2", AttendanceStatus.Presente),
            SessionAttendance("stu-3", AttendanceStatus.Ausente),
        )
    ),
)



