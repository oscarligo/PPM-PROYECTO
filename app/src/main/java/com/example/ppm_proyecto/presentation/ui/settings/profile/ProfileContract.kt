package com.example.ppm_proyecto.presentation.ui.settings.profile

import com.example.ppm_proyecto.domain.models.user.User

object ProfileContract {
    data class State(
        val user: User? = null,
        val isLoading: Boolean = false,
        val error: String = "",
        val successMessage: String = "",
        val name: String = "",
        val profileImageUrl: String = "",
        val showImageUrlDialog: Boolean = false, // Nuevo: controlar el diálogo desde el ViewModel
        val tempImageUrl: String = "", // Nuevo: URL temporal mientras se edita
        val loaded: Boolean = false, // Nuevo: bandera para indicar si los datos fueron cargados
        val clipboardText: String? = null // Nuevo: texto a copiar (evento de un solo uso)
    )

    sealed class Intent {
        data class LoadUser(val userId: String) : Intent()
        data class SetName(val value: String) : Intent()
        data class SetProfileImageUrl(val value: String) : Intent()
        object SaveChanges : Intent()
        data class CopyToClipboard(val text: String) : Intent()
        object ClearClipboardEvent : Intent() // Nuevo: limpiar el evento después de procesarlo
        object OpenImageUrlDialog : Intent() // Nuevo: abrir diálogo
        object CloseImageUrlDialog : Intent() // Nuevo: cerrar diálogo
        data class SetTempImageUrl(val value: String) : Intent() // Nuevo: actualizar URL temporal
        object SaveImageUrl : Intent() // Nuevo: guardar URL desde el diálogo

        data object RefreshUserData: Intent()
    }
}
