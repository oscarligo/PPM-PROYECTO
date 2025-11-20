package com.example.ppm_proyecto.domain.repository.nfc

import com.example.ppm_proyecto.core.util.Result

/**
 * Repositorio para todas las operaciones relacionadas con NFC
 */
interface NfcRepository {
    /**
     * Vincula un tag NFC a la cuenta de un profesor
     * @param userId ID del usuario (profesor)
     * @param nfcTagId ID del tag NFC detectado
     * @return Result<Unit> indicando éxito o error
     */
    suspend fun linkNfcTagToTeacher(userId: String, nfcTagId: String): Result<Unit>

    /**
     * Busca un profesor por el ID de su tag NFC vinculado
     * @param nfcTagId ID del tag NFC
     * @return ID del profesor o null si no existe
     */
    suspend fun findTeacherByNfcTag(nfcTagId: String): Result<String?>

    /**
     * Marca la asistencia de un estudiante cuando acerca su dispositivo al tag del profesor
     * Este método:
     * 1. Busca al profesor por el ID del tag
     * 2. Obtiene los cursos del profesor
     * 3. Busca la sesión activa que coincida con la fecha/hora actual
     * 4. Marca la asistencia del estudiante en esa sesión
     *
     * @param nfcTagId ID del tag NFC del profesor
     * @param studentId ID del estudiante
     * @return Result<Boolean> true si se marcó exitosamente, false si no hay sesión activa
     */
    suspend fun markAttendanceViaTeacherTag(nfcTagId: String, studentId: String): Result<Boolean>

    /**
     * Verifica si un tag NFC ya está vinculado a algún usuario
     * @param nfcTagId ID del tag NFC
     * @return Result<Boolean> true si ya está vinculado, false si está disponible
     */
    suspend fun isNfcTagAlreadyLinked(nfcTagId: String): Result<Boolean>
}

