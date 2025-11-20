package com.example.ppm_proyecto.domain.repository.nfc

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.data.remote.nfc.NfcRemoteDataSource
import javax.inject.Inject

/**
 * Implementación del repositorio NFC
 */
class NfcRepositoryImpl @Inject constructor(
    private val remoteDataSource: NfcRemoteDataSource
) : NfcRepository {

    override suspend fun linkNfcTagToTeacher(userId: String, nfcTagId: String): Result<Unit> {
        return try {
            remoteDataSource.linkNfcTagToUser(userId, nfcTagId)
            Result.Ok(Unit)
        } catch (e: Exception) {
            Result.Err(e)
        }
    }

    override suspend fun findTeacherByNfcTag(nfcTagId: String): Result<String?> {
        return try {
            val teacherId = remoteDataSource.findTeacherByNfcTag(nfcTagId)
            Result.Ok(teacherId)
        } catch (e: Exception) {
            Result.Err(e)
        }
    }

    override suspend fun markAttendanceViaTeacherTag(nfcTagId: String, studentId: String): Result<Boolean> {
        return try {
            val success = remoteDataSource.markAttendanceViaTeacherTag(nfcTagId, studentId)
            Result.Ok(success)
        } catch (e: Exception) {
            Result.Err(e)
        }
    }

    override suspend fun isNfcTagAlreadyLinked(nfcTagId: String): Result<Boolean> {
        return try {
            val isLinked = remoteDataSource.isNfcTagAlreadyLinked(nfcTagId)
            Result.Ok(isLinked)
        } catch (e: Exception) {
            Result.Err(e)
        }
    }
}

