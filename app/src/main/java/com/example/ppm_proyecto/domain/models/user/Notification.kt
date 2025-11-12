package com.example.ppm_proyecto.domain.models.user

import com.google.firebase.Timestamp

data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val date: Timestamp = Timestamp.now(),
    val isRead: Boolean = false,
)