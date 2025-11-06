package com.example.ppm_proyecto.core.util

sealed interface Result<out T> {
    data class Ok<T>(val value: T) : Result<T>
    data class Err(val throwable: Throwable) : Result<Nothing>
}