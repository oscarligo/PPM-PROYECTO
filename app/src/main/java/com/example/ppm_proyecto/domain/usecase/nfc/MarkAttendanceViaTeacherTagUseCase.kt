package com.example.ppm_proyecto.domain.usecase.nfc

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepository
import javax.inject.Inject

/**
 * Use case para marcar asistencia automáticamente cuando un estudiante
 * acerca su dispositivo al tag NFC de un profesor
 *
 * Este use case:
 * 1. Busca al profesor por el ID del tag
 * 2. Obtiene los cursos del profesor
 * 3. Busca la sesión activa que coincida con el día y hora actual
 * 4. Marca la asistencia del estudiante en esa sesión
 */
class MarkAttendanceViaTeacherTagUseCase @Inject constructor(
    private val repository: NfcRepository
) {
    suspend operator fun invoke(nfcTagId: String, studentId: String): Result<Boolean> {
        return repository.markAttendanceViaTeacherTag(nfcTagId, studentId)
    }
}

