package com.selvas.infrastructure.database.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone
import java.time.LocalDateTime


object TracksTable : Table("tracks") {
    val id = uuid("id")
    val title = varchar("title", 150)
    val duration = integer("duration")  // Duración en segundos
    val albumId = uuid("album_id").references(AlbumesTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(id)
}