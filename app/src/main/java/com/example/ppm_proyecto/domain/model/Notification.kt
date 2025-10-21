package com.example.ppm_proyecto.domain.model


data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val date: String,
    val isRead: Boolean
)