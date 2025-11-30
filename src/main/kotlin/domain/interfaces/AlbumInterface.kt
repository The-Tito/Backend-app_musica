package com.selvas.domain.interfaces

import com.selvas.domain.models.Album
import com.selvas.domain.models.Artist


interface AlbumInterface {
    suspend fun getAlbumById(id: String): Album
    suspend fun getAllAlbums(): List<Album>
    suspend fun getAlbumsByArtist(artistId: String): List<Album>
    suspend fun createAlbum(album: Album): Album
    suspend fun updateAlbum(id: String, album: Album): Album
    suspend fun deleteAlbum(id: String): Boolean
}