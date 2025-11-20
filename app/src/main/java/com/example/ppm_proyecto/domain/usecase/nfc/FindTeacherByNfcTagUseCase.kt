package com.example.ppm_proyecto.domain.usecase.nfc

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepository
import javax.inject.Inject

/**
 * Use case para buscar un profesor por el ID de su tag NFC
 */
class FindTeacherByNfcTagUseCase @Inject constructor(
    private val repository: NfcRepository
) {
    suspend operator fun invoke(nfcTagId: String): Result<String?> {
        return repository.findTeacherByNfcTag(nfcTagId)
    }
}

