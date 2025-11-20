package com.example.ppm_proyecto.domain.usecase.nfc

import com.example.ppm_proyecto.core.util.Result
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepository
import javax.inject.Inject

/**
 * Use case para verificar si un tag NFC ya está vinculado a algún usuario
 * Útil para validar antes de vincular un nuevo tag
 */
class IsNfcTagAlreadyLinkedUseCase @Inject constructor(
    private val repository: NfcRepository
) {
    suspend operator fun invoke(nfcTagId: String): Result<Boolean> {
        return repository.isNfcTagAlreadyLinked(nfcTagId)
    }
}

