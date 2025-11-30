package com.selvas.domain.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID


data class Artist(
    val id: UUID,
    val name: String,
    val genre: String?,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?
)

