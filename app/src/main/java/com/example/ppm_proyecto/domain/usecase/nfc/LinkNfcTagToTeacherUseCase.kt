package com.example.ppm_proyecto.domain.usecase.nfc

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepository
import javax.inject.Inject

/**
 * Use case para vincular un tag NFC a la cuenta de un profesor
 */
class LinkNfcTagToTeacherUseCase @Inject constructor(
    private val repository: NfcRepository
) {
    suspend operator fun invoke(userId: String, nfcTagId: String): Result<Unit> {
        return repository.linkNfcTagToTeacher(userId, nfcTagId)
    }
}

