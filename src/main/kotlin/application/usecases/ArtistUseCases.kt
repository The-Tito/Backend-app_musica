package com.selvas.application.usecases

import com.selvas.application.dto.ArtistResponse
import com.selvas.application.dto.CreateArtistRequest
import com.selvas.application.dto.UpdateArtistRequest
import com.selvas.domain.interfaces.ArtistInterface
import com.selvas.domain.models.Artist
import com.selvas.infrastructure.repositories.ArtistRepositoryImpl
import java.util.UUID


class ArtistUseCase(
    private val artistRepository: ArtistInterface
){

    suspend fun createArtist(request: CreateArtistRequest): ArtistResponse {

        val artist = Artist(
            id = UUID.randomUUID(),
            name = request.name,
            genre = request.genre,
            createdAt = null,
            updatedAt = null
        )


        val createdArtist = artistRepository.createArtists(artist)

        return ArtistResponse(
            id = createdArtist.id.toString(),
            name = createdArtist.name,
            genre = createdArtist.genre,
            createdAt = createdArtist.createdAt.toString(),
            updatedAt = createdArtist.updatedAt.toString()
        )
    }

    suspend fun getArtistById(id: String): ArtistResponse {
        val artist = artistRepository.getArtistById(id)
        return artist.toResponse()
    }

    suspend fun getAllArtists(): List<ArtistResponse> {
        val artists = artistRepository.getAllArtists()
        return artists.map { it.toResponse() }
    }

    suspend fun updateArtist(id: String, request: UpdateArtistRequest): ArtistResponse {

        val currentArtist = artistRepository.getArtistById(id)


        val updatedArtist = Artist(
            id = currentArtist.id,
            name = request.name ?: currentArtist.name,
            genre = request.genre ?: currentArtist.genre,
            createdAt = currentArtist.createdAt,
            updatedAt = currentArtist.updatedAt
        )

        val artist = artistRepository.updateArtist(id, updatedArtist)
        return artist.toResponse()
    }

    suspend fun deleteArtist(id: String): Boolean {
        return artistRepository.deleteArtist(id)
    }

    private fun Artist.toResponse() = ArtistResponse(
        id = this.id.toString(),
        name = this.name,
        genre = this.genre,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}