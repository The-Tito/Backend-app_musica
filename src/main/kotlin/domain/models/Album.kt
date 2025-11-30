package com.selvas.domain.models

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID

data class Album(
    val id: UUID,
    val title: String,
    val releaseYear: Int,
    val artistId: UUID,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?
)
