package com.selvas.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
)
