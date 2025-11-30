package com.selvas.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class CreateTrackRequest(
    val title: String,
    val duration: Int,
    @Serializable(with = StringOrNumberSerializer::class)
    val albumId: String
)

@Serializable
data class UpdateTrackRequest(
    val title: String?,
    val duration: Int?
)

@Serializable
data class TrackResponse(
    val id: String,
    val title: String,
    val duration: Int,
    val durationFormatted: String,
    val albumId: String,
    val albumTitle: String?,
    val artistName: String?,
    val createdAt: String,
    val updatedAt: String
)