package com.selvas.infrastructure.repositories

import com.selvas.domain.interfaces.TrackInterface
import com.selvas.domain.models.Track
import com.selvas.infrastructure.database.tables.AlbumesTable
import com.selvas.infrastructure.database.tables.TracksTable
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

class TrackRepositoryImpl : TrackInterface {

    private fun ResultRow.toTrack() = Track(
        id = this[TracksTable.id],
        title = this[TracksTable.title],
        duration = this[TracksTable.duration],
        albumId = this[TracksTable.albumId],
        createdAt = this[TracksTable.createdAt],
        updatedAt = this[TracksTable.updatedAt]
    )

    override suspend fun getTrackById(id: String): Track = dbQuery {
        try {
            TracksTable
                .selectAll()
                .where { TracksTable.id eq UUID.fromString(id) }
                .map { it.toTrack() }
                .singleOrNull()
                ?: throw NoSuchElementException("Track no encontrado: $id")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllTracks(): List<Track> = dbQuery {
        TracksTable
            .selectAll()
            .orderBy(TracksTable.title to SortOrder.ASC)
            .map { it.toTrack() }
    }

    override suspend fun getTracksByAlbum(albumId: String): List<Track> = dbQuery {
        try {
            val uuid = UUID.fromString(albumId)


            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Álbum no encontrado: $albumId")

            TracksTable
                .selectAll()
                .where { TracksTable.albumId eq uuid }
                .orderBy(TracksTable.id to SortOrder.ASC)
                .map { it.toTrack() }

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID de álbum inválido: $albumId no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createTrack(track: Track): Track = dbQuery {
        try {

            AlbumesTable
                .selectAll()
                .where { AlbumesTable.id eq track.albumId }
                .singleOrNull()
                ?: throw NoSuchElementException("Álbum no encontrado: ${track.albumId}")

            val now = OffsetDateTime.now()

            TracksTable.insert {
                it[id] = track.id
                it[title] = track.title
                it[duration] = track.duration
                it[albumId] = track.albumId
                it[createdAt] = now
                it[updatedAt] = now
            }

            track.copy(
                createdAt = now,
                updatedAt = now
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateTrack(id: String, track: Track): Track = dbQuery {
        try {
            val uuid = UUID.fromString(id)


            TracksTable
                .selectAll()
                .where { TracksTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Track no encontrado: $id")

            val now = OffsetDateTime.now()

            TracksTable.update({ TracksTable.id eq uuid }) {
                it[title] = track.title
                it[duration] = track.duration
                it[updatedAt] = now
            }


            TracksTable
                .selectAll()
                .where { TracksTable.id eq uuid }
                .map { it.toTrack() }
                .single()

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteTrack(id: String): Boolean = dbQuery {
        try {
            val uuid = UUID.fromString(id)


            TracksTable
                .selectAll()
                .where { TracksTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Track no encontrado: $id")

            val deletedRows = TracksTable.deleteWhere { TracksTable.id eq uuid }
            deletedRows > 0

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }
}