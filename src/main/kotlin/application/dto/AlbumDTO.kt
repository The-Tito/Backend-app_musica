package com.selvas.application.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object StringOrNumberSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("StringOrNumber", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        return try {
            decoder.decodeString()
        } catch (e: Exception) {
            // Si falla como String, intenta como número
            decoder.decodeDouble().toString()
        }
    }
}

@Serializable
data class CreateAlbumRequest(
    val title: String,
    val releaseYear: Int,
    @Serializable(with = StringOrNumberSerializer::class)
    val artistId: String
)

@Serializable
data class UpdateAlbumRequest(
    val title: String?,
    val releaseYear: Int?
)

@kotlinx.serialization.Serializable
data class AlbumResponse(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistId: String,
    val artistName: String?,
    val createdAt: String,
    val updatedAt: String
)