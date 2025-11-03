package com.example.ppm_proyecto.data.repository.student

import com.example.ppm_proyecto.domain.models.course.Course
import com.example.ppm_proyecto.domain.models.course.CourseSession
import com.example.ppm_proyecto.domain.models.user.User
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : StudentRepository {

    override suspend fun getStudentData(studentId: String): Result<User?> = runCatching {
        val snapshot = db.collection("users").document(studentId).get().await()
        snapshot.toObject(User::class.java)
    }

    override suspend fun getCourses(studentId: String): Result<List<Course>> = runCatching {
        val enrollments = db.collection("enrollments")
            .whereEqualTo("studentId", studentId)
            .get()
            .await()

        val courseIds = enrollments.documents.mapNotNull { it.getString("courseId") }

        if (courseIds.isEmpty()) return@runCatching emptyList()

        val coursesSnapshot = db.collection("courses")
            .whereIn("id", courseIds)
            .get()
            .await()

        coursesSnapshot.toObjects(Course::class.java)
    }

    override suspend fun getAttendance(courseId: String): Result<List<CourseSession>> = runCatching {
        val snapshot = db.collection("sessions")
            .whereEqualTo("courseId", courseId)
            .get()
            .await()

        snapshot.toObjects(CourseSession::class.java)
    }
}