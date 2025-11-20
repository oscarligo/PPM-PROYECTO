package com.example.ppm_proyecto.domain.usecase.teacher

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository
import javax.inject.Inject

class MarkStudentAttendanceViaTeacherTagUseCase @Inject constructor(
    private val repository: TeacherRepository
) {
    suspend operator fun invoke(nfcTagId: String, studentId: String, status: String): Result<Boolean> =
        try { Result.Ok(repository.markStudentAttendanceViaTeacherTag(nfcTagId, studentId, status)) }
        catch (t: Throwable) { Result.Err(t) }
}

