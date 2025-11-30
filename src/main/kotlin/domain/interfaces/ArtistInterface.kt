package com.selvas.domain.interfaces

import com.selvas.domain.models.Artist


interface ArtistInterface {
    suspend fun getArtistById(id: String): Artist
    suspend fun getAllArtists(): List<Artist>
    suspend fun createArtists(artist: Artist): Artist
    suspend fun deleteArtist(id: String): Boolean
    suspend fun updateArtist(id: String, artist: Artist): Artist
}