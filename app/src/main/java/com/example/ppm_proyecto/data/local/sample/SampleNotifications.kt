package com.example.ppm_proyecto.data.local.sample

import com.example.ppm_proyecto.domain.models.user.Notification

val sampleNotifications: List<Notification> = listOf(
    Notification(
        id = "n-001",
        title = "Recordatorio de clase",
        message = "No olvides la clase de Programación Móvil mañana a las 9:00",
        date = "2024-10-07",
        isRead = false
    ),
    Notification(
        id = "n-002",
        title = "Cambio de aula",
        message = "La clase de Álgebra se trasladó al aula 203",
        date = "2024-10-05",
        isRead = true
    )
)

