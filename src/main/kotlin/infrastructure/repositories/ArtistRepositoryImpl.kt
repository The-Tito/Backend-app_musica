package com.selvas.infrastructure.repositories

import com.selvas.domain.interfaces.ArtistInterface
import com.selvas.domain.models.Artist
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

class ArtistRepositoryImpl: ArtistInterface {

    private fun ResultRow.toArtist()= Artist(
        id = this[ArtistsTable.id],
        name = this[ArtistsTable.name],
        genre = this[ArtistsTable.genre],
        createdAt = this[ArtistsTable.createdAt],
        updatedAt = this[ArtistsTable.updatedAt]
    )

    override suspend fun getArtistById(id: String): Artist = dbQuery {
        try {
            ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq UUID.fromString(id) }
                .map { it.toArtist() }
                .singleOrNull()
                ?: throw NoSuchElementException("Artista no encontrado: $id")
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun getAllArtists(): List<Artist> = dbQuery {
        ArtistsTable
            .selectAll()
            .orderBy(ArtistsTable.name to SortOrder.ASC)
            .map { it.toArtist() }
    }

    override suspend fun createArtists(artist: Artist): Artist = dbQuery {
        val now = OffsetDateTime.now()

        ArtistsTable.insert {
            it[id] = artist.id
            it[name] = artist.name
            it[genre] = artist.genre
            it[createdAt] = now
            it[updatedAt] = now
        }

        artist.copy(
            createdAt = now,
            updatedAt = now
        )
    }

    override suspend fun updateArtist(id: String, artist: Artist): Artist = dbQuery {
        try {
            val uuid = UUID.fromString(id)

            val exists = ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Artista no encontrado: $id")

            val now = OffsetDateTime.now()

            ArtistsTable.update({ ArtistsTable.id eq uuid }) {
                artist.name.let { name -> it[ArtistsTable.name] = name }
                it[genre] = artist.genre
                it[updatedAt] = now
            }

            ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq uuid }
                .map { it.toArtist() }
                .single()

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteArtist(id: String): Boolean = dbQuery {
        try {
            val uuid = UUID.fromString(id)


            val exists = ArtistsTable
                .selectAll()
                .where { ArtistsTable.id eq uuid }
                .singleOrNull()
                ?: throw NoSuchElementException("Artista no encontrado: $id")

            val deletedRows = ArtistsTable.deleteWhere { ArtistsTable.id eq uuid }

            deletedRows > 0

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("ID inválido: $id no es un UUID válido")
        } catch (e: Exception) {
            throw e
        }
    }
}