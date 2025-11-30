package com.selvas.application.dto

import kotlinx.serialization.Serializable


@Serializable
data class CreateArtistRequest(
    val name: String,
    val genre: String,
)

@Serializable
data class UpdateArtistRequest(
    val name: String?,
    val genre: String?
)

@Serializable
data class ArtistResponse(
    val id: String,
    val name: String,
    val genre: String?,
    val createdAt: String,
    val updatedAt: String
)