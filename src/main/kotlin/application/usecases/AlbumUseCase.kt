package com.selvas.application.usecases

import com.selvas.application.dto.AlbumResponse
import com.selvas.application.dto.CreateAlbumRequest
import com.selvas.application.dto.UpdateAlbumRequest
import com.selvas.domain.interfaces.AlbumInterface
import com.selvas.domain.interfaces.ArtistInterface
import com.selvas.domain.models.Album
import com.selvas.infrastructure.repositories.AlbumRepositoryImpl
import com.selvas.infrastructure.repositories.ArtistRepositoryImpl
import java.util.UUID

class AlbumUseCase(
    private val albumRepository: AlbumInterface,
    private val artistRepository: ArtistInterface,
) {
    suspend fun createAlbum(request: CreateAlbumRequest): AlbumResponse {
        val album = Album(
            id = UUID.randomUUID(),
            title = request.title,
            releaseYear = request.releaseYear,
            artistId = UUID.fromString(request.artistId),
            createdAt = null,
            updatedAt = null
        )

        val createdAlbum = albumRepository.createAlbum(album)
        return createdAlbum.toResponse()
    }

    suspend fun getAlbumById(id: String): AlbumResponse {
        val album = albumRepository.getAlbumById(id)
        return album.toResponse()
    }

    suspend fun getAllAlbums(): List<AlbumResponse> {
        val albums = albumRepository.getAllAlbums()
        return albums.map { it.toResponse() }
    }

    suspend fun getAlbumsByArtist(artistId: String): List<AlbumResponse> {
        val albums = albumRepository.getAlbumsByArtist(artistId)
        return albums.map { it.toResponse() }
    }

    suspend fun updateAlbum(id: String, request: UpdateAlbumRequest): AlbumResponse {
        val currentAlbum = albumRepository.getAlbumById(id)

        val updatedAlbum = Album(
            id = currentAlbum.id,
            title = request.title ?: currentAlbum.title,
            releaseYear = request.releaseYear ?: currentAlbum.releaseYear,
            artistId = currentAlbum.artistId,
            createdAt = currentAlbum.createdAt,
            updatedAt = currentAlbum.updatedAt
        )

        val album = albumRepository.updateAlbum(id, updatedAlbum)
        return album.toResponse()
    }

    suspend fun deleteAlbum(id: String): Boolean {
        return albumRepository.deleteAlbum(id)
    }

    private suspend fun Album.toResponse(): AlbumResponse {

        val artist = try {
            artistRepository.getArtistById(this.artistId.toString())
        } catch (e: Exception) {
            null
        }

        return AlbumResponse(
            id = this.id.toString(),
            title = this.title,
            releaseYear = this.releaseYear,
            artistId = this.artistId.toString(),
            artistName = artist?.name,
            createdAt = this.createdAt.toString(),
            updatedAt = this.updatedAt.toString()
        )
    }
}