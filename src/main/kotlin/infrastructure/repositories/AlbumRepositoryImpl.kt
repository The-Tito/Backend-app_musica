package com.selvas.infrastructure.repositories

import com.selvas.domain.interfaces.AlbumInterface
import com.selvas.domain.models.Album
import com.selvas.infrastructure.database.tables.AlbumesTable
import com.selvas.infrastructure.database.tables.ArtistsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import plugins.DatabaseFactory.dbQuery
import java.time.OffsetDateTime
import java.util.UUID


class AlbumRepositoryImpl : AlbumInterface {

    private fun ResultRow.toAlbum() = Album(
        id = this[AlbumesTable.id],
        title = this[AlbumesTable.title],
        releaseYear = this[AlbumesTable.releaseYear],
        artistId = this[AlbumesTable.artistId],
        createdAt = this[AlbumesTable.createdAt],
        updatedAt = this[AlbumesTable.updatedAt]
    )

    override suspend fun getAlbumById(id: String): Album = dbQuery {
        try {
            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq UUID.fromString(id) }
                .map { it.toAlbum() }
                .singleOrNull()
                ?: throw NoSuchElementException("Álbum no encontrado: $id")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllAlbums(): List<Album> = dbQuery {
        AlbumesTable
            .selectAll()
            .orderBy(AlbumesTable.releaseYear to SortOrder.DESC)
            .map { it.toAlbum() }
    }

    override suspend fun getAlbumsByArtist(artistId: String): List<Album> = dbQuery {
        try {
            val uuid = UUID.fromString(artistId)


            ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Artista no encontrado: $artistId")

            AlbumesTable
                .selectAll()
                .where { AlbumesTable.artistId eq uuid }
                .orderBy(AlbumesTable.releaseYear to SortOrder.DESC)
                .map { it.toAlbum() }

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID de artista inválido: $artistId no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createAlbum(album: Album): Album = dbQuery {
        try {

            ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq album.artistId }
                .singleOrNull()
                ?: throw NoSuchElementException("Artista no encontrado: ${album.artistId}")

            val now = OffsetDateTime.now()

            AlbumesTable.insert {
                it[id] = album.id
                it[title] = album.title
                it[releaseYear] = album.releaseYear
                it[artistId] = album.artistId
                it[createdAt] = now
                it[updatedAt] = now
            }

            album.copy(
                createdAt = now,
                updatedAt = now
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateAlbum(id: String, album: Album): Album = dbQuery {
        try {
            val uuid = UUID.fromString(id)


            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Álbum no encontrado: $id")

            val now = OffsetDateTime.now()

            AlbumesTable.update({ AlbumesTable.id eq uuid }) {
                it[title] = album.title
                it[releaseYear] = album.releaseYear
                it[updatedAt] = now
            }


            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq uuid }
                .map { it.toAlbum() }
                .single()

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteAlbum(id: String): Boolean = dbQuery {
        try {
            val uuid = UUID.fromString(id)


            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Álbum no encontrado: $id")

            val deletedRows = AlbumesTable.deleteWhere { AlbumesTable.id eq uuid }
            deletedRows > 0

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }
}