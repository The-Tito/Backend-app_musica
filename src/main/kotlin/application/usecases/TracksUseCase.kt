package com.selvas.application.usecases

import com.selvas.application.dto.CreateTrackRequest
import com.selvas.application.dto.TrackResponse
import com.selvas.application.dto.UpdateTrackRequest
import com.selvas.domain.interfaces.AlbumInterface
import com.selvas.domain.interfaces.ArtistInterface
import com.selvas.domain.interfaces.TrackInterface
import com.selvas.domain.models.Track
import java.util.UUID

class TracksUseCase(
    private val trackRepository: TrackInterface,
    private val albumRepository: AlbumInterface,
    private val artistRepository: ArtistInterface
) {

    suspend fun createTrack(request: CreateTrackRequest): TrackResponse {
        val track = Track(
            id = UUID.randomUUID(),
            title = request.title,
            duration = request.duration,
            albumId = UUID.fromString(request.albumId),
            createdAt = null,
            updatedAt = null
        )

        val createdTrack = trackRepository.createTrack(track)
        return createdTrack.toResponse()
    }

    suspend fun getTrack(id: String): TrackResponse {
        val track = trackRepository.getTrackById(id)
        return track.toResponse()
    }

    suspend fun getAllTracks(): List<TrackResponse> {
        val tracks = trackRepository.getAllTracks()
        return tracks.map { it.toResponse() }
    }

    suspend fun getTracksByAlbum(albumId: String): List<TrackResponse> {
        val tracks = trackRepository.getTracksByAlbum(albumId)
        return tracks.map { it.toResponse() }
    }

    suspend fun updateTrack(id: String, request: UpdateTrackRequest): TrackResponse {
        val currentTrack = trackRepository.getTrackById(id)

        val updatedTrack = Track(
            id = currentTrack.id,
            title = request.title ?: currentTrack.title,
            duration = request.duration ?: currentTrack.duration,
            albumId = currentTrack.albumId,
            createdAt = currentTrack.createdAt,
            updatedAt = currentTrack.updatedAt
        )

        val track = trackRepository.updateTrack(id, updatedTrack)
        return track.toResponse()
    }

    suspend fun deleteTrack(id: String): Boolean {
        return trackRepository.deleteTrack(id)
    }

    private suspend fun Track.toResponse(): TrackResponse {

        val album = try {
            albumRepository.getAlbumById(this.albumId.toString())
        } catch (e: Exception) {
            null
        }

        val artist = if (album != null) {
            try {
                artistRepository.getArtistById(album.artistId.toString())
            } catch (e: Exception) {
                null
            }
        } else null

        return TrackResponse(
            id = this.id.toString(),
            title = this.title,
            duration = this.duration,
            durationFormatted = formatDuration(this.duration),
            albumId = this.albumId.toString(),
            albumTitle = album?.title,
            artistName = artist?.name,
            createdAt = this.createdAt.toString(),
            updatedAt = this.updatedAt.toString()
        )
    }

    private fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        } else {
            String.format("%02d:%02d", minutes, secs)
        }
    }
}