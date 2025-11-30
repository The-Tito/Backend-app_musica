package com.selvas.domain.interfaces

import com.selvas.domain.models.Artist
import com.selvas.domain.models.Track


interface TrackInterface {
    suspend fun getTrackById(id: String): Track
    suspend fun getAllTracks(): List<Track>
    suspend fun getTracksByAlbum(albumId: String): List<Track>
    suspend fun createTrack(track: Track): Track
    suspend fun updateTrack(id: String, track: Track): Track
    suspend fun deleteTrack(id: String): Boolean
}